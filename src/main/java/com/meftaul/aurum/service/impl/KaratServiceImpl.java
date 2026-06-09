package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.domain.Karat;
import com.meftaul.aurum.repository.KaratRepository;
import com.meftaul.aurum.service.KaratService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Karat save(Karat karat) {
        log.debug("Request to save Karat : {}", karat);
        return karatRepository.save(karat);
    }

    @Override
    public Karat update(Karat karat) {
        log.debug("Request to update Karat : {}", karat);
        return karatRepository.save(karat);
    }

    @Override
    public Optional<Karat> partialUpdate(Karat karat) {
        log.debug("Request to partially update Karat : {}", karat);

        return karatRepository
            .findById(karat.getId())
            .map(existingKarat -> {
                if (karat.getKaratType() != null) {
                    existingKarat.setKaratType(karat.getKaratType());
                }
                if (karat.getPurityPercent() != null) {
                    existingKarat.setPurityPercent(karat.getPurityPercent());
                }

                return existingKarat;
            })
            .map(karatRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Karat> findAll(Pageable pageable) {
        log.debug("Request to get all Karats");
        return karatRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Karat> findOne(Long id) {
        log.debug("Request to get Karat : {}", id);
        return karatRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Karat : {}", id);
        karatRepository.deleteById(id);
    }
}
