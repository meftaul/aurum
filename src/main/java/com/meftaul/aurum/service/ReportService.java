package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.Customer;
import com.meftaul.aurum.repository.CustomerRepository;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.repository.projection.ReportProjection;
import com.meftaul.aurum.repository.projection.TransactionHistoryAmountByTag;
import com.meftaul.aurum.repository.projection.TransactionHistoryAmountByTagAndCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final CustomerRepository customerRepository;

    public ReportService(TransactionHistoryRepository transactionHistoryRepository, CustomerRepository customerRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.customerRepository = customerRepository;
    }

    public List<ReportProjection> getReport(String tag){
        return transactionHistoryRepository.findReport(tag);
    }

    public List<TransactionHistoryAmountByTag> getTxnHistoryAmountByTag(LocalDate startDate, LocalDate endDate){
        return transactionHistoryRepository.totalAmountByTag(startDate, endDate);
    }

    public List<TransactionHistoryAmountByTagAndCustomer> getTxnHistoryAmountByTagAndCustomer(LocalDate startDate, LocalDate endDate, String customerId){
        Customer customer = customerRepository.findOneByCustomId(customerId).get();
        if (customer == null) {
            throw new RuntimeException();
        }
        return transactionHistoryRepository.totalAmountByTagAndCustomerId(startDate, endDate, customer.getId());
    }

    public List<TransactionHistoryAmountByTagAndCustomer> getTopTxnHistoryAmountByCustomer(String startDate, String endDate, String topN){
        return transactionHistoryRepository.topNTotalAmountByCustomerId(startDate, endDate, topN);
    }


}
