package com.meftaul.aurum.repository;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.repository.projection.ReportProjection;
import com.meftaul.aurum.repository.projection.TransactionHistoryAmountByTag;
import com.meftaul.aurum.repository.projection.TransactionHistoryAmountByTagAndCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data  repository for the TransactionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>, JpaSpecificationExecutor<TransactionHistory> {

    List<TransactionHistory> findAllByVoucherNo(String voucherNo);

    @Query(value = "" +
        "SELECT\n" +
        "    EXTRACT(YEAR FROM date_created) AS year,\n" +
        "    EXTRACT(MONTH FROM date_created) AS month,\n" +
        "    EXTRACT(DAY FROM date_created) AS day,\n" +
        "    SUM(amount) AS totalAmount,\n" +
        "    tag\n" +
        "\n" +
        "    FROM transaction_history\n" +
        "    WHERE tag=:tag\n" +
        "\n" +
        "    GROUP BY\n" +
        "    EXTRACT(YEAR FROM date_created),\n" +
        "    EXTRACT(MONTH FROM date_created),\n" +
        "    EXTRACT(DAY FROM date_created)\n" +
        "\n" +
        "    ORDER BY\n" +
        "\n" +
        "    EXTRACT(YEAR FROM date_created) DESC,\n" +
        "    EXTRACT(MONTH FROM date_created) DESC,\n" +
        "    EXTRACT(DAY FROM date_created) DESC", nativeQuery = true)
    List<ReportProjection> findReport(@Param("tag") String tag);

    @Query(value = "SELECT tag, SUM(amount) AS totalAmount" +
        " FROM transaction_history" +
        " WHERE date_created BETWEEN :startDate AND :endDate" +
        " GROUP BY tag", nativeQuery = true)
    List<TransactionHistoryAmountByTag> totalAmountByTag(@Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    @Query(value = "select customer_id as customerId, tag, sum(amount) as totalAmount from transaction_history " +
        " where date_created between :startDate and :endDate' 23:59:59' and customer_id = :customerId" +
        " group by tag, customer_id", nativeQuery = true)
    List<TransactionHistoryAmountByTagAndCustomer> totalAmountByTagAndCustomerId(@Param("startDate") LocalDate startDate,
                                                                                 @Param("endDate") LocalDate endDate,
                                                                                 @Param("customerId") Long customerId);

    @Query(value = "select customer_id as customerId, sum(amount) as totalAmount from transaction_history" +
        " where date_created between :startDate and :endDate" +
        " group by customer_id order by totalAmount desc limit :n", nativeQuery = true)
    List<TransactionHistoryAmountByTagAndCustomer> topNTotalAmountByCustomerId(@Param("startDate") String startDate,
                                                                               @Param("endDate") String endDate,
                                                                               @Param("n") String topN);
    TransactionHistory findByVoucherNoAndTag(String voucherNo, TransactionStatus tag);
}
