package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.service.AurumServiceQueryService;
import com.meftaul.aurum.service.AurumServiceService;
import com.meftaul.aurum.service.criteria.AurumServiceCriteria;
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
 * REST controller for managing {@link com.meftaul.aurum.domain.AurumService}.
 */
@RestController
@RequestMapping("/api/aurum-services")
public class AurumServiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AurumServiceResource.class);

    private static final String ENTITY_NAME = "aurumService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurumServiceService aurumServiceService;

    private final AurumServiceRepository aurumServiceRepository;

    private final AurumServiceQueryService aurumServiceQueryService;

    public AurumServiceResource(
        AurumServiceService aurumServiceService,
        AurumServiceRepository aurumServiceRepository,
        AurumServiceQueryService aurumServiceQueryService
    ) {
        this.aurumServiceService = aurumServiceService;
        this.aurumServiceRepository = aurumServiceRepository;
        this.aurumServiceQueryService = aurumServiceQueryService;
    }

    /**
     * {@code POST  /aurum-services} : Create a new aurumService.
     *
     * @param aurumService the aurumService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurumService, or with status {@code 400 (Bad Request)} if the aurumService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AurumService> createAurumService(@Valid @RequestBody AurumService aurumService) throws URISyntaxException {
        LOG.debug("REST request to save AurumService : {}", aurumService);
        if (aurumService.getId() != null) {
            throw new BadRequestAlertException("A new aurumService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aurumService = aurumServiceService.save(aurumService);
        return ResponseEntity.created(new URI("/api/aurum-services/" + aurumService.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, aurumService.getId().toString()))
            .body(aurumService);
    }

    /**
     * {@code PUT  /aurum-services/:id} : Updates an existing aurumService.
     *
     * @param id the id of the aurumService to save.
     * @param aurumService the aurumService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurumService,
     * or with status {@code 400 (Bad Request)} if the aurumService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurumService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AurumService> updateAurumService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AurumService aurumService
    ) throws URISyntaxException {
        LOG.debug("REST request to update AurumService : {}, {}", id, aurumService);
        if (aurumService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurumService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurumServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aurumService = aurumServiceService.update(aurumService);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aurumService.getId().toString()))
            .body(aurumService);
    }

    /**
     * {@code PATCH  /aurum-services/:id} : Partial updates given fields of an existing aurumService, field will ignore if it is null
     *
     * @param id the id of the aurumService to save.
     * @param aurumService the aurumService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurumService,
     * or with status {@code 400 (Bad Request)} if the aurumService is not valid,
     * or with status {@code 404 (Not Found)} if the aurumService is not found,
     * or with status {@code 500 (Internal Server Error)} if the aurumService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AurumService> partialUpdateAurumService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AurumService aurumService
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AurumService partially : {}, {}", id, aurumService);
        if (aurumService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aurumService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aurumServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AurumService> result = aurumServiceService.partialUpdate(aurumService);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aurumService.getId().toString())
        );
    }

    /**
     * {@code GET  /aurum-services} : get all the aurumServices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurumServices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AurumService>> getAllAurumServices(
        AurumServiceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AurumServices by criteria: {}", criteria);

        Page<AurumService> page = aurumServiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aurum-services/count} : count all the aurumServices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAurumServices(AurumServiceCriteria criteria) {
        LOG.debug("REST request to count AurumServices by criteria: {}", criteria);
        return ResponseEntity.ok().body(aurumServiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /aurum-services/:id} : get the "id" aurumService.
     *
     * @param id the id of the aurumService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurumService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AurumService> getAurumService(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AurumService : {}", id);
        Optional<AurumService> aurumService = aurumServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aurumService);
    }

    /**
     * {@code DELETE  /aurum-services/:id} : delete the "id" aurumService.
     *
     * @param id the id of the aurumService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAurumService(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AurumService : {}", id);
        aurumServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
