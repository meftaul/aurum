package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.*; // for static metamodels
import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.service.criteria.TransactionHistoryCriteria;
import com.meftaul.aurum.service.dto.TxnReportDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.List;
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
 * It returns a {@link List} of {@link TransactionHistory} or a {@link Page} of {@link TransactionHistory} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionHistoryQueryService extends QueryService<TransactionHistory> {

    private final Logger log = LoggerFactory.getLogger(TransactionHistoryQueryService.class);

    private final TransactionHistoryRepository transactionHistoryRepository;

    private final EntityManager entityManager;

    public TransactionHistoryQueryService(TransactionHistoryRepository transactionHistoryRepository, EntityManager entityManager) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.entityManager = entityManager;
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

    @Transactional(readOnly = true)
    public TxnReportDto reportByCriteria(TransactionHistoryCriteria criteria) {
        log.debug("build report by criteria : {}", criteria);

        TxnReportDto txnReportDto = new TxnReportDto();

        // 1) Per-tag count + amount totals over the filtered transactions.
        applyTagAggregates(criteria, txnReportDto);

        // 2) Distinct number of vouchers referenced by the filtered transactions.
        txnReportDto.setTotalVoucherCount(countDistinctVouchers(criteria));

        // 3) Per-service-type amount/service-charge breakdown over the vouchers behind those transactions.
        applyServiceTypeAggregates(criteria, txnReportDto);

        // 4) Total payable amount summed over those (distinct, existing) vouchers.
        txnReportDto.setTotalPayableAmount(sumVoucherPayableAmount(criteria));

        txnReportDto.setDue(
            txnReportDto.getTotalPayableAmount().subtract(txnReportDto.getTotalReceived()).subtract(txnReportDto.getTotalDiscount())
        );

        return txnReportDto;
    }

    /** {@code SELECT tag, COUNT(*), SUM(amount) ... GROUP BY tag} over the criteria-filtered transactions. */
    private void applyTagAggregates(TransactionHistoryCriteria criteria, TxnReportDto txnReportDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<TransactionHistory> root = cq.from(TransactionHistory.class);

        cq.multiselect(root.get(TransactionHistory_.tag), cb.count(root), cb.sum(root.get(TransactionHistory_.amount)));
        Predicate predicate = createSpecification(criteria).toPredicate(root, cq, cb);
        if (predicate != null) {
            cq.where(predicate);
        }
        cq.groupBy(root.get(TransactionHistory_.tag));

        for (Tuple row : entityManager.createQuery(cq).getResultList()) {
            TransactionStatus tag = row.get(0, TransactionStatus.class);
            long count = row.get(1, Long.class);
            BigDecimal total = zeroIfNull(row.get(2, BigDecimal.class));
            if (tag == TransactionStatus.RECEIVE) {
                txnReportDto.setReceivedVoucherCount(count);
                txnReportDto.setTotalReceived(total);
            } else if (tag == TransactionStatus.VAT) {
                txnReportDto.setVatVoucherCount(count);
                txnReportDto.setTotalVat(total);
            } else if (tag == TransactionStatus.REFUND) {
                txnReportDto.setRefundVoucherCount(count);
                txnReportDto.setTotalRefund(total);
            } else if (tag == TransactionStatus.DISCOUNT) {
                txnReportDto.setDiscountVoucherCount(count);
                txnReportDto.setTotalDiscount(total);
            }
        }
    }

    /** {@code SELECT COUNT(DISTINCT voucherNo) ...} over the criteria-filtered transactions. */
    private long countDistinctVouchers(TransactionHistoryCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<TransactionHistory> root = cq.from(TransactionHistory.class);

        cq.select(cb.countDistinct(root.get(TransactionHistory_.voucherNo)));
        Predicate predicate = createSpecification(criteria).toPredicate(root, cq, cb);
        if (predicate != null) {
            cq.where(predicate);
        }
        Long count = entityManager.createQuery(cq).getSingleResult();
        return count == null ? 0L : count;
    }

    /**
     * {@code SELECT s.serviceType, SUM(s.amount), SUM(s.serviceCharge) FROM Voucher v JOIN v.aurumServices s
     * WHERE v.voucherNo IN (distinct voucher numbers of the filtered transactions) GROUP BY s.serviceType}.
     */
    private void applyServiceTypeAggregates(TransactionHistoryCriteria criteria, TxnReportDto txnReportDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Voucher> voucher = cq.from(Voucher.class);
        Join<Object, Object> service = voucher.join("aurumServices", JoinType.INNER);

        cq.multiselect(
            service.<String>get("serviceType"),
            cb.sum(service.<BigDecimal>get("amount")),
            cb.sum(service.<BigDecimal>get("serviceCharge"))
        );
        cq.where(cb.in(voucher.<String>get("voucherNo")).value(distinctVoucherNoSubquery(cb, cq, criteria)));
        cq.groupBy(service.<String>get("serviceType"));

        for (Tuple row : entityManager.createQuery(cq).getResultList()) {
            String serviceType = row.get(0, String.class);
            BigDecimal amount = zeroIfNull(row.get(1, BigDecimal.class));
            BigDecimal serviceCharge = zeroIfNull(row.get(2, BigDecimal.class));
            if ("X-Ray".equals(serviceType)) {
                txnReportDto.setxRayAmount(amount);
            } else if ("Hallmark".equals(serviceType)) {
                txnReportDto.setHallMarkAmount(amount);
            } else if ("Normal Melting".equals(serviceType)) {
                txnReportDto.setNormalMeltingAmount(amount);
                txnReportDto.setNormalMeltingServiceChargeAmount(serviceCharge);
            } else if ("Calculated Melting".equals(serviceType)) {
                txnReportDto.setCalculatedMeltingAmount(amount);
                txnReportDto.setCalculatedMeltingServiceChargeAmount(serviceCharge);
            }
        }
    }

    /** {@code SELECT SUM(v.totalPayableAmount) FROM Voucher v WHERE v.voucherNo IN (...distinct filtered voucher numbers...)}. */
    private BigDecimal sumVoucherPayableAmount(TransactionHistoryCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<Voucher> voucher = cq.from(Voucher.class);

        cq.select(cb.sum(voucher.<BigDecimal>get("totalPayableAmount")));
        cq.where(cb.in(voucher.<String>get("voucherNo")).value(distinctVoucherNoSubquery(cb, cq, criteria)));
        return zeroIfNull(entityManager.createQuery(cq).getSingleResult());
    }

    /** Subquery yielding the distinct {@code voucherNo}s of the criteria-filtered transactions, for an {@code IN} clause. */
    private Subquery<String> distinctVoucherNoSubquery(
        CriteriaBuilder cb,
        CriteriaQuery<?> outerQuery,
        TransactionHistoryCriteria criteria
    ) {
        Subquery<String> subquery = outerQuery.subquery(String.class);
        Root<TransactionHistory> subRoot = subquery.from(TransactionHistory.class);
        subquery.select(subRoot.get(TransactionHistory_.voucherNo)).distinct(true);
        Predicate predicate = createSpecification(criteria).toPredicate(subRoot, outerQuery, cb);
        if (predicate != null) {
            subquery.where(predicate);
        }
        return subquery;
    }

    private static BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
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
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
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
