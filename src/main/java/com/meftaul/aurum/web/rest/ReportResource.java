package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.service.MessageService;
import com.meftaul.aurum.service.ReportService;
import com.meftaul.aurum.repository.projection.ReportProjection;
import com.meftaul.aurum.repository.projection.TransactionHistoryAmountByTag;
import com.meftaul.aurum.repository.projection.TransactionHistoryAmountByTagAndCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * ReportResource controller
 */
@RestController
@RequestMapping("/api/report")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    private final ReportService reportService;
    private final MessageService messageService;

    public ReportResource(ReportService reportService, MessageService messageService) {
        this.reportService = reportService;
        this.messageService = messageService;
    }

    /**
    * GET getSale
    */
    @GetMapping("/get-report")
    public ResponseEntity<List<ReportProjection>> getSale(@RequestParam String tag) {
        return ResponseEntity.ok().body(reportService.getReport(tag));
    }

    @GetMapping("/get-sale-tag")
    public ResponseEntity<List<TransactionHistoryAmountByTag>> getSaleByTag(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam String tag) {
        return ResponseEntity.ok().body(reportService.getTxnHistoryAmountByTag(startDate, endDate));
    }

    @GetMapping("/get-sale-by-customer")
    public ResponseEntity<List<TransactionHistoryAmountByTagAndCustomer>> getSaleByTagAndCustomer(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam String customCustomerId) {
        return ResponseEntity.ok().body(reportService.getTxnHistoryAmountByTagAndCustomer(startDate, endDate, customCustomerId));
    }

    @GetMapping("/get-sale-top-n-customer")
    public ResponseEntity<List<TransactionHistoryAmountByTagAndCustomer>> getTopNCustomer(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String topN) {
        return ResponseEntity.ok().body(reportService.getTopTxnHistoryAmountByCustomer(startDate, endDate, topN));
    }


    @GetMapping("/get-sms")
    public ResponseEntity<String> sendSms() {
        return ResponseEntity.ok().body(messageService.sendSms());
    }

}
