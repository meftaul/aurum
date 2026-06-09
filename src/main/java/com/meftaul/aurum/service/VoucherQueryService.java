package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.*; // for static metamodels
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.criteria.VoucherCriteria;
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
 * Service for executing complex queries for {@link Voucher} entities in the database.
 * The main input is a {@link VoucherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Voucher} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VoucherQueryService extends QueryService<Voucher> {

    private static final Logger LOG = LoggerFactory.getLogger(VoucherQueryService.class);

    private final VoucherRepository voucherRepository;

    public VoucherQueryService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    /**
     * Return a {@link Page} of {@link Voucher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Voucher> findByCriteria(VoucherCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Voucher> specification = createSpecification(criteria);
        return voucherRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VoucherCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Voucher> specification = createSpecification(criteria);
        return voucherRepository.count(specification);
    }

    /**
     * Function to convert {@link VoucherCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Voucher> createSpecification(VoucherCriteria criteria) {
        Specification<Voucher> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Voucher_.id),
                buildStringSpecification(criteria.getVoucherNo(), Voucher_.voucherNo),
                buildRangeSpecification(criteria.getCustomerId(), Voucher_.customerId),
                buildRangeSpecification(criteria.getCalculatedTotalAmount(), Voucher_.calculatedTotalAmount),
                buildRangeSpecification(criteria.getVat(), Voucher_.vat),
                buildRangeSpecification(criteria.getDisountAmount(), Voucher_.disountAmount),
                buildSpecification(criteria.getStatus(), Voucher_.status),
                buildRangeSpecification(criteria.getTotalPayableAmount(), Voucher_.totalPayableAmount),
                buildRangeSpecification(criteria.getDateCreated(), Voucher_.dateCreated),
                buildStringSpecification(criteria.getAddedBy(), Voucher_.addedBy),
                buildStringSpecification(criteria.getBoxNumber(), Voucher_.boxNumber),
                buildRangeSpecification(criteria.getDeliveryDate(), Voucher_.deliveryDate),
                buildSpecification(criteria.getDeliveryStatus(), Voucher_.deliveryStatus),
                buildSpecification(criteria.getAurumServiceId(), root ->
                    root.join(Voucher_.aurumServices, JoinType.LEFT).get(AurumService_.id)
                )
            );
        }
        return specification;
    }
}
