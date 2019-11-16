package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.security.SecurityUtils;
import com.meftaul.aurum.service.dto.CustomVoucherDto;
import com.meftaul.aurum.service.dto.VoucherViewerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomVoucherService {

    private final Logger log = LoggerFactory.getLogger(CustomVoucherService.class);

    private final VoucherRepository voucherRepository;
    private final AurumServiceRepository aurumServiceRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public CustomVoucherService(VoucherRepository voucherRepository, AurumServiceRepository aurumServiceRepository, TransactionHistoryRepository transactionHistoryRepository) {
        this.voucherRepository = voucherRepository;
        this.aurumServiceRepository = aurumServiceRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
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
        voucher.setDateCreated(LocalDate.now());


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

        return voucherRepository.save(savedVoucher);
    }

    private TransactionHistory createTxnHistory(Voucher voucher, BigDecimal amount, TransactionStatus tag) {

        TransactionHistory transactionHistory = new TransactionHistory();

        transactionHistory.setAddedBy(SecurityUtils.getCurrentUserLogin().get());
        transactionHistory.setAmount(voucher.getTotalPayableAmount());
        transactionHistory.setDateCreated(LocalDate.now());
        transactionHistory.setCustomerId(voucher.getCustomerId());
        transactionHistory.setVoucherNo(voucher.getVoucherNo());
        transactionHistory.setTag(tag);

        transactionHistory.setAmount(amount);

        return transactionHistoryRepository.save(transactionHistory);

    }

    private String getVoucherNumber(String userId) {
        LocalDate today = LocalDate.now();
        Long todayCount = this.voucherRepository.countByDateCreated(today);
        return String.valueOf(today.getYear() % 100) + String.valueOf(today.getMonthValue()) + String.valueOf(today.getDayOfMonth()) + String.format("%05d", todayCount);
    }

}
