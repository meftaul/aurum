package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.service.MessageService;
import com.meftaul.aurum.service.ReportService;
import com.meftaul.aurum.service.dto.ReportProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/get-sms")
    public ResponseEntity<String> sendSms() {
        return ResponseEntity.ok().body(messageService.sendSms());
    }

}
