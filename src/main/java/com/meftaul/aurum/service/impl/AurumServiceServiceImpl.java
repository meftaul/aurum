package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.AurumServiceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AurumService update(AurumService aurumService) {
        log.debug("Request to update AurumService : {}", aurumService);
        return aurumServiceRepository.save(aurumService);
    }

    @Override
    public Optional<AurumService> partialUpdate(AurumService aurumService) {
        log.debug("Request to partially update AurumService : {}", aurumService);

        return aurumServiceRepository
            .findById(aurumService.getId())
            .map(existingAurumService -> {
                if (aurumService.getServiceType() != null) {
                    existingAurumService.setServiceType(aurumService.getServiceType());
                }
                if (aurumService.getItemName() != null) {
                    existingAurumService.setItemName(aurumService.getItemName());
                }
                if (aurumService.getQuantity() != null) {
                    existingAurumService.setQuantity(aurumService.getQuantity());
                }
                if (aurumService.getWeight() != null) {
                    existingAurumService.setWeight(aurumService.getWeight());
                }
                if (aurumService.getRate() != null) {
                    existingAurumService.setRate(aurumService.getRate());
                }
                if (aurumService.getAmount() != null) {
                    existingAurumService.setAmount(aurumService.getAmount());
                }
                if (aurumService.getServiceName() != null) {
                    existingAurumService.setServiceName(aurumService.getServiceName());
                }
                if (aurumService.getKaratType() != null) {
                    existingAurumService.setKaratType(aurumService.getKaratType());
                }
                if (aurumService.getExpectedKaratType() != null) {
                    existingAurumService.setExpectedKaratType(aurumService.getExpectedKaratType());
                }
                if (aurumService.getAddedAlloy() != null) {
                    existingAurumService.setAddedAlloy(aurumService.getAddedAlloy());
                }
                if (aurumService.getAlloyQuantity() != null) {
                    existingAurumService.setAlloyQuantity(aurumService.getAlloyQuantity());
                }
                if (aurumService.getServiceCharge() != null) {
                    existingAurumService.setServiceCharge(aurumService.getServiceCharge());
                }
                if (aurumService.getFreeCheck() != null) {
                    existingAurumService.setFreeCheck(aurumService.getFreeCheck());
                }
                if (aurumService.getHallMarkedText() != null) {
                    existingAurumService.setHallMarkedText(aurumService.getHallMarkedText());
                }
                if (aurumService.getWeightOfFreeCheck() != null) {
                    existingAurumService.setWeightOfFreeCheck(aurumService.getWeightOfFreeCheck());
                }

                return existingAurumService;
            })
            .map(aurumServiceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AurumService> findAll(Pageable pageable) {
        log.debug("Request to get all AurumServices");
        return aurumServiceRepository.findAll(pageable);
    }

    public Page<AurumService> findAllWithEagerRelationships(Pageable pageable) {
        return aurumServiceRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AurumService> findOne(Long id) {
        log.debug("Request to get AurumService : {}", id);
        return aurumServiceRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AurumService : {}", id);
        aurumServiceRepository.deleteById(id);
    }
}
