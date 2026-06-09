package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.Voucher;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.meftaul.aurum.domain.Voucher}.
 */
public interface VoucherService {
    /**
     * Save a voucher.
     *
     * @param voucher the entity to save.
     * @return the persisted entity.
     */
    Voucher save(Voucher voucher);

    /**
     * Updates a voucher.
     *
     * @param voucher the entity to update.
     * @return the persisted entity.
     */
    Voucher update(Voucher voucher);

    /**
     * Partially updates a voucher.
     *
     * @param voucher the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Voucher> partialUpdate(Voucher voucher);

    /**
     * Get the "id" voucher.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Voucher> findOne(Long id);

    /**
     * Delete the "id" voucher.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
