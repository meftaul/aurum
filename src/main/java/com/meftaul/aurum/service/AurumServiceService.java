package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.AurumService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AurumService}.
 */
public interface AurumServiceService {

    /**
     * Save a aurumService.
     *
     * @param aurumService the entity to save.
     * @return the persisted entity.
     */
    AurumService save(AurumService aurumService);

    /**
     * Get all the aurumServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AurumService> findAll(Pageable pageable);


    /**
     * Get the "id" aurumService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AurumService> findOne(Long id);

    /**
     * Delete the "id" aurumService.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
