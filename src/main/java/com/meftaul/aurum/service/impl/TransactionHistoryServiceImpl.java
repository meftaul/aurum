package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.TransactionHistoryService;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.errors.BusinessValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TransactionHistory}.
 */
@Service
@Transactional
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final Logger log = LoggerFactory.getLogger(TransactionHistoryServiceImpl.class);

    private final TransactionHistoryRepository transactionHistoryRepository;

    private final VoucherRepository voucherRepository;

    public TransactionHistoryServiceImpl(TransactionHistoryRepository transactionHistoryRepository,
                                         VoucherRepository voucherRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.voucherRepository = voucherRepository;
    }

    @Override
    public TransactionHistory save(TransactionHistory transactionHistory) {
        log.debug("Request to save TransactionHistory : {}", transactionHistory);

        Voucher voucher = voucherRepository.findByVoucherNo(transactionHistory.getVoucherNo());

        if (voucher == null) {
            throw new BusinessValidationException("Invalid voucher.", "transactionHistory", "invalidVoucher");
        }

        TransactionHistory rcvHistory =
            transactionHistoryRepository.findByVoucherNoAndTag(transactionHistory.getVoucherNo(), TransactionStatus.RECEIVE);

        TransactionHistory discountHistory =
            transactionHistoryRepository.findByVoucherNoAndTag(transactionHistory.getVoucherNo(), TransactionStatus.DISCOUNT);

        if (transactionHistory.getAmount().compareTo(rcvHistory.getAmount()) == 1) {
            throw new BusinessValidationException("Invalid amount.", "transactionHistory", "invalidAmount");
        }

        rcvHistory.setAmount(rcvHistory.getAmount().add(discountHistory.getAmount()).subtract(transactionHistory.getAmount()));
        transactionHistoryRepository.save(rcvHistory);

        discountHistory.setAmount(transactionHistory.getAmount());
        transactionHistoryRepository.save(discountHistory);

        voucher.setDisountAmount(discountHistory.getAmount());
        voucher.setTotalPayableAmount(rcvHistory.getAmount());
        voucherRepository.save(voucher);

        return discountHistory;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionHistory> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionHistories");
        return transactionHistoryRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionHistory> findOne(Long id) {
        log.debug("Request to get TransactionHistory : {}", id);
        return transactionHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionHistory : {}", id);
        transactionHistoryRepository.deleteById(id);
    }
}
