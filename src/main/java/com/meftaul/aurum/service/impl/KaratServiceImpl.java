package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.service.KaratService;
import com.meftaul.aurum.domain.Karat;
import com.meftaul.aurum.repository.KaratRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Karat}.
 */
@Service
@Transactional
public class KaratServiceImpl implements KaratService {

    private final Logger log = LoggerFactory.getLogger(KaratServiceImpl.class);

    private final KaratRepository karatRepository;

    public KaratServiceImpl(KaratRepository karatRepository) {
        this.karatRepository = karatRepository;
    }

    /**
     * Save a karat.
     *
     * @param karat the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Karat save(Karat karat) {
        log.debug("Request to save Karat : {}", karat);
        return karatRepository.save(karat);
    }

    /**
     * Get all the karats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Karat> findAll(Pageable pageable) {
        log.debug("Request to get all Karats");
        return karatRepository.findAll(pageable);
    }


    /**
     * Get one karat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Karat> findOne(Long id) {
        log.debug("Request to get Karat : {}", id);
        return karatRepository.findById(id);
    }

    /**
     * Delete the karat by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Karat : {}", id);
        karatRepository.deleteById(id);
    }
}
