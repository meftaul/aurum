package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.TransactionHistory;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.meftaul.aurum.domain.TransactionHistory}.
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
     * Updates a transactionHistory.
     *
     * @param transactionHistory the entity to update.
     * @return the persisted entity.
     */
    TransactionHistory update(TransactionHistory transactionHistory);

    /**
     * Partially updates a transactionHistory.
     *
     * @param transactionHistory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionHistory> partialUpdate(TransactionHistory transactionHistory);

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
