package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Voucher save(Voucher voucher) {
        log.debug("Request to save Voucher : {}", voucher);

        Voucher savedVoucher = voucherRepository.save(voucher);

        for (AurumService s : voucher.getAurumServices()) {
            s.setVoucher(savedVoucher);
            aurumServiceRepository.save(s);
            savedVoucher.addAurumService(s);
        }



        if (savedVoucher.getVat() != null) {

        }


        return voucherRepository.save(savedVoucher);
    }

    private TransactionHistory getTxnHistory(Voucher voucher) {

        TransactionHistory transactionHistory = new TransactionHistory();

        transactionHistory.setAddedBy(SecurityUtils.getCurrentUserLogin().get());
        transactionHistory.setAmount(voucher.getTotalPayableAmount());
        transactionHistory.setDateCreated(LocalDate.now());
        transactionHistory.setCustomerId(voucher.getCustomerId());
        transactionHistory.setVoucherNo(voucher.getVoucherNo());

        return transactionHistory;

    }

}
