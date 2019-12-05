package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.service.dto.ReportProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    /*SELECT EXTRACT(DAY FROM DATE_CREATED) AS DAY,  SUM(AMOUNT) AS TOTAL
    FROM TRANSACTION_HISTORY
    GROUP BY EXTRACT(DAY FROM DATE_CREATED)*/

    /*
    SELECT
    EXTRACT(YEAR FROM date_created) AS year,
    EXTRACT(MONTH FROM date_created) AS month,
    EXTRACT(DAY FROM date_created) AS day,
    SUM(amount) AS total

    FROM transaction_history
    WHERE tag='RECEIVE'

    GROUP BY
    EXTRACT(YEAR FROM date_created),
    EXTRACT(MONTH FROM date_created),
    EXTRACT(DAY FROM date_created)

    ORDER BY

    EXTRACT(YEAR FROM date_created) DESC,
    EXTRACT(MONTH FROM date_created) DESC,
    EXTRACT(DAY FROM date_created) DESC;
    */

    private final TransactionHistoryRepository transactionHistoryRepository;

    public ReportService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public List<ReportProjection> getReport(String tag){
        return transactionHistoryRepository.findReport(tag);
    }
}
