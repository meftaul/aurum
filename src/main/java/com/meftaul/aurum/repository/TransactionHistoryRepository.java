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

}
