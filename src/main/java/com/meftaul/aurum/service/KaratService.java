package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.Karat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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
