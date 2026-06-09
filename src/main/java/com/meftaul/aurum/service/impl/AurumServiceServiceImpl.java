package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.service.AurumServiceService;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.repository.AurumServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AurumService}.
 */
@Service
@Transactional
public class AurumServiceServiceImpl implements AurumServiceService {

    private final Logger log = LoggerFactory.getLogger(AurumServiceServiceImpl.class);

    private final AurumServiceRepository aurumServiceRepository;

    public AurumServiceServiceImpl(AurumServiceRepository aurumServiceRepository) {
        this.aurumServiceRepository = aurumServiceRepository;
    }

    @Override
    public AurumService save(AurumService aurumService) {
        log.debug("Request to save AurumService : {}", aurumService);
        return aurumServiceRepository.save(aurumService);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AurumService> findAll(Pageable pageable) {
        log.debug("Request to get all AurumServices");
        return aurumServiceRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AurumService> findOne(Long id) {
        log.debug("Request to get AurumService : {}", id);
        return aurumServiceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AurumService : {}", id);
        aurumServiceRepository.deleteById(id);
    }
}
