package com.meftaul.aurum.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.*; // for static metamodels
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.dto.AurumServiceCriteria;

/**
 * Service for executing complex queries for {@link AurumService} entities in the database.
 * The main input is a {@link AurumServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AurumService} or a {@link Page} of {@link AurumService} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AurumServiceQueryService extends QueryService<AurumService> {

    private final Logger log = LoggerFactory.getLogger(AurumServiceQueryService.class);

    private final AurumServiceRepository aurumServiceRepository;

    public AurumServiceQueryService(AurumServiceRepository aurumServiceRepository) {
        this.aurumServiceRepository = aurumServiceRepository;
    }

    /**
     * Return a {@link List} of {@link AurumService} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AurumService> findByCriteria(AurumServiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AurumService> specification = createSpecification(criteria);
        return aurumServiceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AurumService} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AurumService> findByCriteria(AurumServiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AurumService> specification = createSpecification(criteria);
        return aurumServiceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AurumServiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AurumService> specification = createSpecification(criteria);
        return aurumServiceRepository.count(specification);
    }

    /**
     * Function to convert {@link AurumServiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AurumService> createSpecification(AurumServiceCriteria criteria) {
        Specification<AurumService> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AurumService_.id));
            }
            if (criteria.getServiceType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceType(), AurumService_.serviceType));
            }
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), AurumService_.itemName));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), AurumService_.quantity));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AurumService_.weight));
            }
            if (criteria.getRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRate(), AurumService_.rate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), AurumService_.amount));
            }
            if (criteria.getServiceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceName(), AurumService_.serviceName));
            }
            if (criteria.getKaratType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKaratType(), AurumService_.karatType));
            }
            if (criteria.getExpectedKaratType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExpectedKaratType(), AurumService_.expectedKaratType));
            }
            if (criteria.getAddedAlloy() != null) {
                specification = specification.and(buildSpecification(criteria.getAddedAlloy(), AurumService_.addedAlloy));
            }
            if (criteria.getAlloyQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAlloyQuantity(), AurumService_.alloyQuantity));
            }
            if (criteria.getServiceCharge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getServiceCharge(), AurumService_.serviceCharge));
            }
            if (criteria.getFreeCheck() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFreeCheck(), AurumService_.freeCheck));
            }
            if (criteria.getHallMarkedText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHallMarkedText(), AurumService_.hallMarkedText));
            }
            if (criteria.getVoucherId() != null) {
                specification = specification.and(buildSpecification(criteria.getVoucherId(),
                    root -> root.join(AurumService_.voucher, JoinType.LEFT).get(Voucher_.id)));
            }
        }
        return specification;
    }
}
