package com.meftaul.aurum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    /*SELECT EXTRACT(DAY FROM DATE_CREATED) AS DAY,  SUM(AMOUNT) AS TOTAL
    FROM TRANSACTION_HISTORY
    GROUP BY EXTRACT(DAY FROM DATE_CREATED)*/

    /*
    SELECT
    EXTRACT(YEAR FROM DATE_CREATED) AS YEAR,
    EXTRACT(MONTH FROM DATE_CREATED) AS MONTH,
    EXTRACT(DAY FROM DATE_CREATED) AS DAY,
    SUM(AMOUNT) AS TOTAL

    FROM TRANSACTION_HISTORY
    WHERE TAG='RECEIVE'

    GROUP BY
    EXTRACT(YEAR FROM DATE_CREATED),
    EXTRACT(MONTH FROM DATE_CREATED),
    EXTRACT(DAY FROM DATE_CREATED)

    ORDER BY

    EXTRACT(YEAR FROM DATE_CREATED) DESC,
    EXTRACT(MONTH FROM DATE_CREATED) DESC,
    EXTRACT(DAY FROM DATE_CREATED) DESC
    */

}
