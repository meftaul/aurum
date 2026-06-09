package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.*; // for static metamodels
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.criteria.AurumServiceCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AurumService} entities in the database.
 * The main input is a {@link AurumServiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AurumService} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AurumServiceQueryService extends QueryService<AurumService> {

    private static final Logger LOG = LoggerFactory.getLogger(AurumServiceQueryService.class);

    private final AurumServiceRepository aurumServiceRepository;

    public AurumServiceQueryService(AurumServiceRepository aurumServiceRepository) {
        this.aurumServiceRepository = aurumServiceRepository;
    }

    /**
     * Return a {@link Page} of {@link AurumService} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AurumService> findByCriteria(AurumServiceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
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
        LOG.debug("count by criteria : {}", criteria);
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
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AurumService_.id),
                buildStringSpecification(criteria.getServiceType(), AurumService_.serviceType),
                buildStringSpecification(criteria.getItemName(), AurumService_.itemName),
                buildRangeSpecification(criteria.getQuantity(), AurumService_.quantity),
                buildRangeSpecification(criteria.getWeight(), AurumService_.weight),
                buildRangeSpecification(criteria.getRate(), AurumService_.rate),
                buildRangeSpecification(criteria.getAmount(), AurumService_.amount),
                buildStringSpecification(criteria.getServiceName(), AurumService_.serviceName),
                buildStringSpecification(criteria.getKaratType(), AurumService_.karatType),
                buildStringSpecification(criteria.getExpectedKaratType(), AurumService_.expectedKaratType),
                buildSpecification(criteria.getAddedAlloy(), AurumService_.addedAlloy),
                buildRangeSpecification(criteria.getAlloyQuantity(), AurumService_.alloyQuantity),
                buildRangeSpecification(criteria.getServiceCharge(), AurumService_.serviceCharge),
                buildRangeSpecification(criteria.getFreeCheck(), AurumService_.freeCheck),
                buildStringSpecification(criteria.getHallMarkedText(), AurumService_.hallMarkedText),
                buildStringSpecification(criteria.getWeightOfFreeCheck(), AurumService_.weightOfFreeCheck),
                buildSpecification(criteria.getVoucherId(), root -> root.join(AurumService_.voucher, JoinType.LEFT).get(Voucher_.id))
            );
        }
        return specification;
    }
}
