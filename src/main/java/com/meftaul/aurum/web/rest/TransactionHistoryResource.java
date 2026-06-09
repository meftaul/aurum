package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.repository.TransactionHistoryRepository;
import com.meftaul.aurum.service.TransactionHistoryQueryService;
import com.meftaul.aurum.service.TransactionHistoryService;
import com.meftaul.aurum.service.criteria.TransactionHistoryCriteria;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.meftaul.aurum.domain.TransactionHistory}.
 */
@RestController
@RequestMapping("/api/transaction-histories")
public class TransactionHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionHistoryResource.class);

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
     * {@code POST  /transaction-histories} : Create a new transactionHistory.
     *
     * @param transactionHistory the transactionHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionHistory, or with status {@code 400 (Bad Request)} if the transactionHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionHistory> createTransactionHistory(@Valid @RequestBody TransactionHistory transactionHistory)
        throws URISyntaxException {
        LOG.debug("REST request to save TransactionHistory : {}", transactionHistory);
        if (transactionHistory.getId() != null) {
            throw new BadRequestAlertException("A new transactionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionHistory = transactionHistoryService.save(transactionHistory);
        return ResponseEntity.created(new URI("/api/transaction-histories/" + transactionHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, transactionHistory.getId().toString()))
            .body(transactionHistory);
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
    @PutMapping("/{id}")
    public ResponseEntity<TransactionHistory> updateTransactionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionHistory transactionHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update TransactionHistory : {}, {}", id, transactionHistory);
        if (transactionHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transactionHistory = transactionHistoryService.update(transactionHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionHistory.getId().toString()))
            .body(transactionHistory);
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
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionHistory> partialUpdateTransactionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionHistory transactionHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TransactionHistory partially : {}, {}", id, transactionHistory);
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
    @GetMapping("")
    public ResponseEntity<List<TransactionHistory>> getAllTransactionHistories(
        TransactionHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TransactionHistories by criteria: {}", criteria);

        Page<TransactionHistory> page = transactionHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-histories/count} : count all the transactionHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransactionHistories(TransactionHistoryCriteria criteria) {
        LOG.debug("REST request to count TransactionHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-histories/:id} : get the "id" transactionHistory.
     *
     * @param id the id of the transactionHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionHistory> getTransactionHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TransactionHistory : {}", id);
        Optional<TransactionHistory> transactionHistory = transactionHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionHistory);
    }

    /**
     * {@code DELETE  /transaction-histories/:id} : delete the "id" transactionHistory.
     *
     * @param id the id of the transactionHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TransactionHistory : {}", id);
        transactionHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
