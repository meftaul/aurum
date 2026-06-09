package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.TransactionHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TransactionHistory}.
 */
public interface TransactionHistoryService {

    /**
     * Save a transactionHistory.
     *
     * @param transactionHistory the entity to save.
     * @return the persisted entity.
     */
    TransactionHistory save(TransactionHistory transactionHistory);

    /**
     * Get all the transactionHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionHistory> findAll(Pageable pageable);


    /**
     * Get the "id" transactionHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionHistory> findOne(Long id);

    /**
     * Delete the "id" transactionHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
