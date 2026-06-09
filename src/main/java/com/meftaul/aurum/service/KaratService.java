package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.Karat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Karat}.
 */
public interface KaratService {
    /**
     * Save a karat.
     *
     * @param karat the entity to save.
     * @return the persisted entity.
     */
    Karat save(Karat karat);

    /**
     * Updates a karat.
     *
     * @param karat the entity to update.
     * @return the persisted entity.
     */
    Karat update(Karat karat);

    /**
     * Partially updates a karat.
     *
     * @param karat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Karat> partialUpdate(Karat karat);

    /**
     * Get all the karats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Karat> findAll(Pageable pageable);

    /**
     * Get the "id" karat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Karat> findOne(Long id);

    /**
     * Delete the "id" karat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
