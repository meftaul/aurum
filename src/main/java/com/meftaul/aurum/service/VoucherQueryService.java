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

import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.domain.*; // for static metamodels
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.dto.VoucherCriteria;

/**
 * Service for executing complex queries for {@link Voucher} entities in the database.
 * The main input is a {@link VoucherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Voucher} or a {@link Page} of {@link Voucher} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VoucherQueryService extends QueryService<Voucher> {

    private final Logger log = LoggerFactory.getLogger(VoucherQueryService.class);

    private final VoucherRepository voucherRepository;

    public VoucherQueryService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    /**
     * Return a {@link List} of {@link Voucher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Voucher> findByCriteria(VoucherCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Voucher> specification = createSpecification(criteria);
        return voucherRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Voucher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Voucher> findByCriteria(VoucherCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
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
        log.debug("count by criteria : {}", criteria);
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
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Voucher_.id));
            }
            if (criteria.getVoucherNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVoucherNo(), Voucher_.voucherNo));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerId(), Voucher_.customerId));
            }
            if (criteria.getCalculatedTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCalculatedTotalAmount(), Voucher_.calculatedTotalAmount));
            }
            if (criteria.getVat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVat(), Voucher_.vat));
            }
            if (criteria.getDisountAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDisountAmount(), Voucher_.disountAmount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Voucher_.status));
            }
            if (criteria.getTotalPayableAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPayableAmount(), Voucher_.totalPayableAmount));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Voucher_.dateCreated));
            }
            if (criteria.getAddedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddedBy(), Voucher_.addedBy));
            }
            if (criteria.getBoxNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBoxNumber(), Voucher_.boxNumber));
            }
            if (criteria.getDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeliveryDate(), Voucher_.deliveryDate));
            }
            if (criteria.getDeliveryStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getDeliveryStatus(), Voucher_.deliveryStatus));
            }
            if (criteria.getAurumServiceId() != null) {
                specification = specification.and(buildSpecification(criteria.getAurumServiceId(),
                    root -> root.join(Voucher_.aurumServices, JoinType.LEFT).get(AurumService_.id)));
            }
        }
        return specification;
    }
}
