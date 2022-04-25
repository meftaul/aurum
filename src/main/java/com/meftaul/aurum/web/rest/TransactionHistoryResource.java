package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.enumeration.TransactionStatus;
import com.meftaul.aurum.security.AuthoritiesConstants;
import com.meftaul.aurum.service.TransactionHistoryService;
import com.meftaul.aurum.service.dto.TxnReportDto;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;
import com.meftaul.aurum.service.dto.TransactionHistoryCriteria;
import com.meftaul.aurum.service.TransactionHistoryQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

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

    private final TransactionHistoryQueryService transactionHistoryQueryService;

    public TransactionHistoryResource(TransactionHistoryService transactionHistoryService, TransactionHistoryQueryService transactionHistoryQueryService) {
        this.transactionHistoryService = transactionHistoryService;
        this.transactionHistoryQueryService = transactionHistoryQueryService;
    }

    /**
     * {@code POST  /transaction-histories} : Create a new transactionHistory.
     *
     * @param transactionHistory the transactionHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionHistory, or with status {@code 400 (Bad Request)} if the transactionHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/transaction-histories")
    public ResponseEntity<TransactionHistory> createTransactionHistory(@Valid @RequestBody TransactionHistory transactionHistory) throws URISyntaxException {
        log.debug("REST request to save TransactionHistory : {}", transactionHistory);
        if (transactionHistory.getId() != null) {
            throw new BadRequestAlertException("A new transactionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionHistory result = transactionHistoryService.save(transactionHistory);
        return ResponseEntity.created(new URI("/api/transaction-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * {@code PUT  /transaction-histories} : Updates an existing transactionHistory.
     *
     * @param transactionHistory the transactionHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionHistory,
     * or with status {@code 400 (Bad Request)} if the transactionHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Secured({AuthoritiesConstants.ADMIN})
    @PutMapping("/transaction-histories")
    public ResponseEntity<TransactionHistory> updateTransactionHistory(@Valid @RequestBody TransactionHistory transactionHistory) throws URISyntaxException {
        log.debug("REST request to update TransactionHistory : {}", transactionHistory);
        if (transactionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!transactionHistory.getTag().equals(TransactionStatus.DISCOUNT)) {
            throw new BadRequestAlertException("Only discount type is allowed.", ENTITY_NAME, "notDiscount");
        }
        TransactionHistory result = transactionHistoryService.save(transactionHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /transaction-histories} : get all the transactionHistories.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionHistories in body.
     */
    @GetMapping("/transaction-histories")
    public ResponseEntity<List<TransactionHistory>> getAllTransactionHistories(TransactionHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TransactionHistories by criteria: {}", criteria);
        Page<TransactionHistory> page = transactionHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/transaction-report")
    public ResponseEntity<TxnReportDto> getAllTransactionReport(TransactionHistoryCriteria criteria) {
        log.debug("REST request to get TransactionHistories by criteria: {}", criteria);
        TxnReportDto txnReportDto = transactionHistoryQueryService.reportByCriteria(criteria);
        /*HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest());*/
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

    /**
     * {@code DELETE  /transaction-histories/:id} : delete the "id" transactionHistory.
     *
     * @param id the id of the transactionHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/transaction-histories/{id}")
    public ResponseEntity<Void> deleteTransactionHistory(@PathVariable Long id) {
        log.debug("REST request to delete TransactionHistory : {}", id);
        transactionHistoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }*/
}
