package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.security.AuthoritiesConstants;
import com.meftaul.aurum.service.TransactionHistoryQueryService;
import com.meftaul.aurum.service.TransactionHistoryService;
import com.meftaul.aurum.service.criteria.TransactionHistoryCriteria;
import com.meftaul.aurum.service.dto.TxnReportDto;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.meftaul.aurum.domain.TransactionHistory}.
 */
@RestController
@RequestMapping("/api")
public class TransactionHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TransactionHistoryResource.class);

    private static final String ENTITY_NAME = "transactionHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionHistoryService transactionHistoryService;

    private final TransactionHistoryRepository transactionHistoryRepository;

    private final TransactionHistoryQueryService transactionHistoryQueryService;

    public TransactionHistoryResource(
        TransactionHistoryService transactionHistoryService,
        TransactionHistoryRepository transactionHistoryRepository,
        TransactionHistoryQueryService transactionHistoryQueryService
    ) {
        this.transactionHistoryService = transactionHistoryService;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.transactionHistoryQueryService = transactionHistoryQueryService;
    }

    /**
     * {@code PUT  /transaction-histories/:id} : Updates an existing transactionHistory.
     *
     * @param id the id of the transactionHistory to save.
     * @param transactionHistory the transactionHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionHistory,
     * or with status {@code 400 (Bad Request)} if the transactionHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Secured({ AuthoritiesConstants.ADMIN })
    @PutMapping("/transaction-histories")
    public ResponseEntity<TransactionHistory> updateTransactionHistory(@Valid @RequestBody TransactionHistory transactionHistory)
        throws URISyntaxException {
        log.debug("REST request to update TransactionHistory : {}", transactionHistory);
        if (transactionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TransactionHistory result = transactionHistoryService.save(transactionHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-histories/:id} : Partial updates given fields of an existing transactionHistory, field will ignore if it is null
     *
     * @param id the id of the transactionHistory to save.
     * @param transactionHistory the transactionHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionHistory,
     * or with status {@code 400 (Bad Request)} if the transactionHistory is not valid,
     * or with status {@code 404 (Not Found)} if the transactionHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionHistory> partialUpdateTransactionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionHistory transactionHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionHistory partially : {}, {}", id, transactionHistory);
        if (transactionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionHistory> result = transactionHistoryService.partialUpdate(transactionHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-histories} : get all the transactionHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionHistories in body.
     */
    @GetMapping("/transaction-histories")
    public ResponseEntity<List<TransactionHistory>> getAllTransactionHistories(
        TransactionHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TransactionHistories by criteria: {}", criteria);
        Page<TransactionHistory> page = transactionHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/transaction-report")
    public ResponseEntity<TxnReportDto> getAllTransactionReport(TransactionHistoryCriteria criteria) {
        log.debug("REST request to get TransactionHistories by criteria: {}", criteria);
        TxnReportDto txnReportDto = transactionHistoryQueryService.reportByCriteria(criteria);
        return ResponseEntity.ok().body(txnReportDto);
    }

    /**
     * {@code GET  /transaction-histories/count} : count all the transactionHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-histories/count")
    public ResponseEntity<Long> countTransactionHistories(TransactionHistoryCriteria criteria) {
        log.debug("REST request to count TransactionHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-histories/:id} : get the "id" transactionHistory.
     *
     * @param id the id of the transactionHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-histories/{id}")
    public ResponseEntity<TransactionHistory> getTransactionHistory(@PathVariable Long id) {
        log.debug("REST request to get TransactionHistory : {}", id);
        Optional<TransactionHistory> transactionHistory = transactionHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionHistory);
    }
}
