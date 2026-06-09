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

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.*; // for static metamodels
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.service.dto.TransactionHistoryCriteria;

/**
 * Service for executing complex queries for {@link TransactionHistory} entities in the database.
 * The main input is a {@link TransactionHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionHistory} or a {@link Page} of {@link TransactionHistory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionHistoryQueryService extends QueryService<TransactionHistory> {

    private final Logger log = LoggerFactory.getLogger(TransactionHistoryQueryService.class);

    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoryQueryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionHistory> findByCriteria(TransactionHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionHistory> specification = createSpecification(criteria);
        return transactionHistoryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TransactionHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionHistory> findByCriteria(TransactionHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionHistory> specification = createSpecification(criteria);
        return transactionHistoryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionHistory> specification = createSpecification(criteria);
        return transactionHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionHistory> createSpecification(TransactionHistoryCriteria criteria) {
        Specification<TransactionHistory> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionHistory_.id));
            }
            if (criteria.getVoucherNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVoucherNo(), TransactionHistory_.voucherNo));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), TransactionHistory_.amount));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), TransactionHistory_.dateCreated));
            }
            if (criteria.getTag() != null) {
                specification = specification.and(buildSpecification(criteria.getTag(), TransactionHistory_.tag));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerId(), TransactionHistory_.customerId));
            }
            if (criteria.getAddedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddedBy(), TransactionHistory_.addedBy));
            }
        }
        return specification;
    }
}
