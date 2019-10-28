package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.Rate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Rate}.
 */
public interface RateService {

    /**
     * Save a rate.
     *
     * @param rate the entity to save.
     * @return the persisted entity.
     */
    Rate save(Rate rate);

    /**
     * Get all the rates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Rate> findAll(Pageable pageable);


    /**
     * Get the "id" rate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rate> findOne(Long id);

    /**
     * Delete the "id" rate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
