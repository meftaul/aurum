package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.AurumService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.meftaul.aurum.domain.AurumService}.
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
     * Updates a aurumService.
     *
     * @param aurumService the entity to update.
     * @return the persisted entity.
     */
    AurumService update(AurumService aurumService);

    /**
     * Partially updates a aurumService.
     *
     * @param aurumService the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AurumService> partialUpdate(AurumService aurumService);

    /**
     * Get all the aurumServices with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AurumService> findAllWithEagerRelationships(Pageable pageable);

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
