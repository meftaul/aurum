package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Customer;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.VoucherStatus;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.repository.CustomerRepository;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.security.SecurityUtils;
import com.meftaul.aurum.service.dto.CustomVoucherDto;
import com.meftaul.aurum.service.dto.TransactionDto;
import com.meftaul.aurum.service.dto.VoucherViewerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomVoucherService {

    private final Logger log = LoggerFactory.getLogger(CustomVoucherService.class);

    private final VoucherRepository voucherRepository;
    private final AurumServiceRepository aurumServiceRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final CustomerRepository customerRepository;

    public CustomVoucherService(VoucherRepository voucherRepository, AurumServiceRepository aurumServiceRepository, TransactionHistoryRepository transactionHistoryRepository, CustomerRepository customerRepository) {
        this.voucherRepository = voucherRepository;
        this.aurumServiceRepository = aurumServiceRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.customerRepository = customerRepository;
    }

    public Optional<VoucherViewerDto> findByVoucherNo(String voucherNo) {

        Voucher voucher = this.voucherRepository.findByVoucherNo(voucherNo);
        List<TransactionHistory> txnHistory = this.transactionHistoryRepository.findAllByVoucherNo(voucherNo);

        VoucherViewerDto voucherViewerDto = new VoucherViewerDto();
        voucherViewerDto.setVoucherInfo(voucher);
        voucherViewerDto.setTxnHistory(txnHistory);

        BigDecimal sum = BigDecimal.ZERO;
        //calculate due amount
        if (txnHistory.size() > 0) {
            for (TransactionHistory singleTxnHistory : txnHistory) {
                if (singleTxnHistory.getTag().equals(TransactionStatus.RECEIVE)) {
                    sum = sum.add(singleTxnHistory.getAmount());
                }
            }
        }

        BigDecimal due = voucher.getTotalPayableAmount().subtract(sum);

        voucherViewerDto.setTotalAmount(voucher.getTotalPayableAmount());
        voucherViewerDto.setDueAmount(due);

        return Optional.of(voucherViewerDto);
    }

    public Voucher save(CustomVoucherDto voucherDto) {
        log.debug("Request to save Voucher : {}", voucherDto);
        Voucher voucher = voucherDto.getVoucher();

        //generate voucher number
        voucher.setVoucherNo(getVoucherNumber(String.valueOf(voucher.getCustomerId())));
        voucher.setDateCreated(Instant.now());


        Voucher savedVoucher = voucherRepository.save(voucher);

        for (AurumService s : voucher.getAurumServices()) {
            s.setVoucher(savedVoucher);
            aurumServiceRepository.save(s);
            savedVoucher.addAurumService(s);
        }

        createTxnHistory(savedVoucher, voucherDto.getPaidAmount(), TransactionStatus.RECEIVE);
        if (savedVoucher.getVat() != null) {
            createTxnHistory(savedVoucher, savedVoucher.getVat(), TransactionStatus.VAT);
        }
        if (savedVoucher.getDisountAmount() != null) {
            createTxnHistory(savedVoucher, savedVoucher.getDisountAmount(), TransactionStatus.DISCOUNT);
        }

        updateCustomerPoint(voucher.getCustomerId());

        return voucherRepository.save(savedVoucher);
    }

    public TransactionHistory saveCustomTransaction(TransactionDto txnDto) {
        String voucherNo = txnDto.getTransactionHistory().getVoucherNo();
        Voucher voucher = voucherRepository.findByVoucherNo(voucherNo);

        if (voucher != null && txnDto.getVoucherStatus().equals(VoucherStatus.PAID)) {
            voucher.setStatus(VoucherStatus.PAID);
        }

        if (voucher != null && txnDto.getDeliveryStatus()) {
            voucher.setDeliveryStatus(txnDto.getDeliveryStatus());
        }

        voucherRepository.save(voucher);

        return transactionHistoryRepository.save(txnDto.getTransactionHistory());
    }

    private void updateCustomerPoint(Long customerId) {
        Customer customer = customerRepository.getOne(customerId);
        Long existingPoint = customer.getTotalPoint();
        if (existingPoint == null || existingPoint == 0L) {
            existingPoint = 5L;
        } else {
            existingPoint += 5L;
        }
        customer.setTotalPoint(existingPoint);
        customerRepository.save(customer);
    }

    private TransactionHistory createTxnHistory(Voucher voucher, BigDecimal amount, TransactionStatus tag) {

        TransactionHistory transactionHistory = new TransactionHistory();

        transactionHistory.setAddedBy(SecurityUtils.getCurrentUserLogin().get());
        transactionHistory.setAmount(voucher.getTotalPayableAmount());
        transactionHistory.setDateCreated(Instant.now());
        transactionHistory.setCustomerId(voucher.getCustomerId());
        transactionHistory.setVoucherNo(voucher.getVoucherNo());
        transactionHistory.setTag(tag);

        transactionHistory.setAmount(amount);

        return transactionHistoryRepository.save(transactionHistory);

    }

    private String getVoucherNumber(String userId) {
        LocalDate today = LocalDate.now();
        Instant instant = today.atStartOfDay().toInstant(ZoneOffset.UTC);

        /*System.out.println("=================================");
        System.out.println(instant);
        System.out.println("=================================");*/

        Long todayCount = this.voucherRepository.countByDateCreatedAfter(instant) + 1;
        return String.valueOf(today.getYear() % 100) + String.format("%02d", today.getMonthValue()) + String.format("%02d", today.getDayOfMonth())  + String.format("%05d", todayCount);
    }

}
