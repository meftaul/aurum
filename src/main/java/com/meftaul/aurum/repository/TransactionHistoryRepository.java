package com.meftaul.aurum.repository;
import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.service.dto.ReportProjection;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
        "    EXTRACT(YEAR FROM DATE_CREATED) AS year,\n" +
        "    EXTRACT(MONTH FROM DATE_CREATED) AS month,\n" +
        "    EXTRACT(DAY FROM DATE_CREATED) AS day,\n" +
        "    SUM(AMOUNT) AS totalAmount,\n" +
        "    tag\n" +
        "\n" +
        "    FROM TRANSACTION_HISTORY\n" +
        "    WHERE TAG=:tag\n" +
        "\n" +
        "    GROUP BY\n" +
        "    EXTRACT(YEAR FROM DATE_CREATED),\n" +
        "    EXTRACT(MONTH FROM DATE_CREATED),\n" +
        "    EXTRACT(DAY FROM DATE_CREATED)\n" +
        "\n" +
        "    ORDER BY\n" +
        "\n" +
        "    EXTRACT(YEAR FROM DATE_CREATED) DESC,\n" +
        "    EXTRACT(MONTH FROM DATE_CREATED) DESC,\n" +
        "    EXTRACT(DAY FROM DATE_CREATED) DESC", nativeQuery = true)
    List<ReportProjection> findReport(@Param("tag") String tag);

}
