package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.service.AurumServiceService;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.meftaul.aurum.domain.AurumService}.
 */
@RestController
@RequestMapping("/api")
public class AurumServiceResource {

    private final Logger log = LoggerFactory.getLogger(AurumServiceResource.class);

    private static final String ENTITY_NAME = "aurumService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AurumServiceService aurumServiceService;

    public AurumServiceResource(AurumServiceService aurumServiceService) {
        this.aurumServiceService = aurumServiceService;
    }

    /**
     * {@code POST  /aurum-services} : Create a new aurumService.
     *
     * @param aurumService the aurumService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aurumService, or with status {@code 400 (Bad Request)} if the aurumService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aurum-services")
    public ResponseEntity<AurumService> createAurumService(@Valid @RequestBody AurumService aurumService) throws URISyntaxException {
        log.debug("REST request to save AurumService : {}", aurumService);
        if (aurumService.getId() != null) {
            throw new BadRequestAlertException("A new aurumService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AurumService result = aurumServiceService.save(aurumService);
        return ResponseEntity.created(new URI("/api/aurum-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aurum-services} : Updates an existing aurumService.
     *
     * @param aurumService the aurumService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aurumService,
     * or with status {@code 400 (Bad Request)} if the aurumService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aurumService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aurum-services")
    public ResponseEntity<AurumService> updateAurumService(@Valid @RequestBody AurumService aurumService) throws URISyntaxException {
        log.debug("REST request to update AurumService : {}", aurumService);
        if (aurumService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AurumService result = aurumServiceService.save(aurumService);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aurumService.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /aurum-services} : get all the aurumServices.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aurumServices in body.
     */
    @GetMapping("/aurum-services")
    public ResponseEntity<List<AurumService>> getAllAurumServices(Pageable pageable) {
        log.debug("REST request to get a page of AurumServices");
        Page<AurumService> page = aurumServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aurum-services/:id} : get the "id" aurumService.
     *
     * @param id the id of the aurumService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aurumService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aurum-services/{id}")
    public ResponseEntity<AurumService> getAurumService(@PathVariable Long id) {
        log.debug("REST request to get AurumService : {}", id);
        Optional<AurumService> aurumService = aurumServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aurumService);
    }

    /**
     * {@code DELETE  /aurum-services/:id} : delete the "id" aurumService.
     *
     * @param id the id of the aurumService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aurum-services/{id}")
    public ResponseEntity<Void> deleteAurumService(@PathVariable Long id) {
        log.debug("REST request to delete AurumService : {}", id);
        aurumServiceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
