package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.errors.BusinessValidationException;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.TransactionHistoryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TransactionHistory update(TransactionHistory transactionHistory) {
        log.debug("Request to update TransactionHistory : {}", transactionHistory);
        return transactionHistoryRepository.save(transactionHistory);
    }

    @Override
    public Optional<TransactionHistory> partialUpdate(TransactionHistory transactionHistory) {
        log.debug("Request to partially update TransactionHistory : {}", transactionHistory);

        return transactionHistoryRepository
            .findById(transactionHistory.getId())
            .map(existingTransactionHistory -> {
                if (transactionHistory.getVoucherNo() != null) {
                    existingTransactionHistory.setVoucherNo(transactionHistory.getVoucherNo());
                }
                if (transactionHistory.getAmount() != null) {
                    existingTransactionHistory.setAmount(transactionHistory.getAmount());
                }
                if (transactionHistory.getDateCreated() != null) {
                    existingTransactionHistory.setDateCreated(transactionHistory.getDateCreated());
                }
                if (transactionHistory.getTag() != null) {
                    existingTransactionHistory.setTag(transactionHistory.getTag());
                }
                if (transactionHistory.getCustomerId() != null) {
                    existingTransactionHistory.setCustomerId(transactionHistory.getCustomerId());
                }
                if (transactionHistory.getAddedBy() != null) {
                    existingTransactionHistory.setAddedBy(transactionHistory.getAddedBy());
                }

                return existingTransactionHistory;
            })
            .map(transactionHistoryRepository::save);
    }

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
