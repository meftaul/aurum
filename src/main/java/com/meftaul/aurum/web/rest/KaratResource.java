package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.Karat;
import com.meftaul.aurum.repository.KaratRepository;
import com.meftaul.aurum.service.KaratService;
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
 * REST controller for managing {@link com.meftaul.aurum.domain.Karat}.
 */
@RestController
@RequestMapping("/api")
public class KaratResource {

    private final Logger log = LoggerFactory.getLogger(KaratResource.class);

    private static final String ENTITY_NAME = "karat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KaratService karatService;

    private final KaratRepository karatRepository;

    public KaratResource(KaratService karatService, KaratRepository karatRepository) {
        this.karatService = karatService;
        this.karatRepository = karatRepository;
    }

    /**
     * {@code POST  /karats} : Create a new karat.
     *
     * @param karat the karat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new karat, or with status {@code 400 (Bad Request)} if the karat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/karats")
    public ResponseEntity<Karat> createKarat(@Valid @RequestBody Karat karat) throws URISyntaxException {
        log.debug("REST request to save Karat : {}", karat);
        if (karat.getId() != null) {
            throw new BadRequestAlertException("A new karat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Karat result = karatService.save(karat);
        return ResponseEntity
            .created(new URI("/api/karats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /karats/:id} : Updates an existing karat.
     *
     * @param id the id of the karat to save.
     * @param karat the karat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated karat,
     * or with status {@code 400 (Bad Request)} if the karat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the karat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/karats/{id}")
    public ResponseEntity<Karat> updateKarat(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Karat karat)
        throws URISyntaxException {
        log.debug("REST request to update Karat : {}, {}", id, karat);
        if (karat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, karat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!karatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Karat result = karatService.update(karat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, karat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /karats/:id} : Partial updates given fields of an existing karat, field will ignore if it is null
     *
     * @param id the id of the karat to save.
     * @param karat the karat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated karat,
     * or with status {@code 400 (Bad Request)} if the karat is not valid,
     * or with status {@code 404 (Not Found)} if the karat is not found,
     * or with status {@code 500 (Internal Server Error)} if the karat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/karats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Karat> partialUpdateKarat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Karat karat
    ) throws URISyntaxException {
        log.debug("REST request to partial update Karat partially : {}, {}", id, karat);
        if (karat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, karat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!karatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Karat> result = karatService.partialUpdate(karat);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, karat.getId().toString())
        );
    }

    /**
     * {@code GET  /karats} : get all the karats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of karats in body.
     */
    @GetMapping("/karats")
    public ResponseEntity<List<Karat>> getAllKarats(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Karats");
        Page<Karat> page = karatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /karats/:id} : get the "id" karat.
     *
     * @param id the id of the karat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the karat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/karats/{id}")
    public ResponseEntity<Karat> getKarat(@PathVariable Long id) {
        log.debug("REST request to get Karat : {}", id);
        Optional<Karat> karat = karatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(karat);
    }

    /**
     * {@code DELETE  /karats/:id} : delete the "id" karat.
     *
     * @param id the id of the karat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/karats/{id}")
    public ResponseEntity<Void> deleteKarat(@PathVariable Long id) {
        log.debug("REST request to delete Karat : {}", id);
        karatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
