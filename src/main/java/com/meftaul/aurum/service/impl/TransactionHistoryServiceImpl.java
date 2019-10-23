package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.service.TransactionHistoryService;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
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

    public TransactionHistoryServiceImpl(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    /**
     * Save a transactionHistory.
     *
     * @param transactionHistory the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TransactionHistory save(TransactionHistory transactionHistory) {
        log.debug("Request to save TransactionHistory : {}", transactionHistory);
        return transactionHistoryRepository.save(transactionHistory);
    }

    /**
     * Get all the transactionHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionHistory> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionHistories");
        return transactionHistoryRepository.findAll(pageable);
    }


    /**
     * Get one transactionHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionHistory> findOne(Long id) {
        log.debug("Request to get TransactionHistory : {}", id);
        return transactionHistoryRepository.findById(id);
    }

    /**
     * Delete the transactionHistory by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionHistory : {}", id);
        transactionHistoryRepository.deleteById(id);
    }
}
