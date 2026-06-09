package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.Rate;
import com.meftaul.aurum.repository.RateRepository;
import com.meftaul.aurum.service.RateService;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.meftaul.aurum.domain.Rate}.
 */
@RestController
@RequestMapping("/api")
public class RateResource {

    private final Logger log = LoggerFactory.getLogger(RateResource.class);

    private static final String ENTITY_NAME = "rate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RateService rateService;

    private final RateRepository rateRepository;

    public RateResource(RateService rateService, RateRepository rateRepository) {
        this.rateService = rateService;
        this.rateRepository = rateRepository;
    }

    /**
     * {@code POST  /rates} : Create a new rate.
     *
     * @param rate the rate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rate, or with status {@code 400 (Bad Request)} if the rate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rates")
    public ResponseEntity<Rate> createRate(@Valid @RequestBody Rate rate) throws URISyntaxException {
        log.debug("REST request to save Rate : {}", rate);
        if (rate.getId() != null) {
            throw new BadRequestAlertException("A new rate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rate result = rateService.save(rate);
        return ResponseEntity
            .created(new URI("/api/rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rates/:id} : Updates an existing rate.
     *
     * @param id the id of the rate to save.
     * @param rate the rate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rate,
     * or with status {@code 400 (Bad Request)} if the rate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rates/{id}")
    public ResponseEntity<Rate> updateRate(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Rate rate)
        throws URISyntaxException {
        log.debug("REST request to update Rate : {}, {}", id, rate);
        if (rate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rate result = rateService.update(rate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rates/:id} : Partial updates given fields of an existing rate, field will ignore if it is null
     *
     * @param id the id of the rate to save.
     * @param rate the rate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rate,
     * or with status {@code 400 (Bad Request)} if the rate is not valid,
     * or with status {@code 404 (Not Found)} if the rate is not found,
     * or with status {@code 500 (Internal Server Error)} if the rate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rate> partialUpdateRate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rate rate
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rate partially : {}, {}", id, rate);
        if (rate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rate> result = rateService.partialUpdate(rate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rate.getId().toString())
        );
    }

    /**
     * {@code GET  /rates} : get all the rates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rates in body.
     */
    @GetMapping("/rates")
    public ResponseEntity<List<Rate>> getAllRates(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rates");
        Page<Rate> page = rateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rates/:id} : get the "id" rate.
     *
     * @param id the id of the rate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rates/{id}")
    public ResponseEntity<Rate> getRate(@PathVariable Long id) {
        log.debug("REST request to get Rate : {}", id);
        Optional<Rate> rate = rateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rate);
    }

    /**
     * {@code DELETE  /rates/:id} : delete the "id" rate.
     *
     * @param id the id of the rate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rates/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        log.debug("REST request to delete Rate : {}", id);
        rateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
