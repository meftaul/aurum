package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.domain.Rate;
import com.meftaul.aurum.repository.RateRepository;
import com.meftaul.aurum.service.RateService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.meftaul.aurum.domain.Rate}.
 */
@Service
@Transactional
public class RateServiceImpl implements RateService {

    private static final Logger LOG = LoggerFactory.getLogger(RateServiceImpl.class);

    private final RateRepository rateRepository;

    public RateServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @Override
    public Rate save(Rate rate) {
        LOG.debug("Request to save Rate : {}", rate);
        return rateRepository.save(rate);
    }

    @Override
    public Rate update(Rate rate) {
        LOG.debug("Request to update Rate : {}", rate);
        return rateRepository.save(rate);
    }

    @Override
    public Optional<Rate> partialUpdate(Rate rate) {
        LOG.debug("Request to partially update Rate : {}", rate);

        return rateRepository
            .findById(rate.getId())
            .map(existingRate -> {
                if (rate.getRateType() != null) {
                    existingRate.setRateType(rate.getRateType());
                }
                if (rate.getUnitPrice() != null) {
                    existingRate.setUnitPrice(rate.getUnitPrice());
                }

                return existingRate;
            })
            .map(rateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rate> findAll(Pageable pageable) {
        LOG.debug("Request to get all Rates");
        return rateRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rate> findOne(Long id) {
        LOG.debug("Request to get Rate : {}", id);
        return rateRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Rate : {}", id);
        rateRepository.deleteById(id);
    }
}
