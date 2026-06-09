package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.*; // for static metamodels
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.service.criteria.TransactionHistoryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TransactionHistory} entities in the database.
 * The main input is a {@link TransactionHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransactionHistory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionHistoryQueryService extends QueryService<TransactionHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionHistoryQueryService.class);

    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoryQueryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    /**
     * Return a {@link Page} of {@link TransactionHistory} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionHistory> findByCriteria(TransactionHistoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
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
        LOG.debug("count by criteria : {}", criteria);
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
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TransactionHistory_.id),
                buildStringSpecification(criteria.getVoucherNo(), TransactionHistory_.voucherNo),
                buildRangeSpecification(criteria.getAmount(), TransactionHistory_.amount),
                buildRangeSpecification(criteria.getDateCreated(), TransactionHistory_.dateCreated),
                buildSpecification(criteria.getTag(), TransactionHistory_.tag),
                buildRangeSpecification(criteria.getCustomerId(), TransactionHistory_.customerId),
                buildStringSpecification(criteria.getAddedBy(), TransactionHistory_.addedBy)
            );
        }
        return specification;
    }
}
