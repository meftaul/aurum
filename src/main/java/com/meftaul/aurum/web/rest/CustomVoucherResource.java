package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.service.CustomVoucherService;
import com.meftaul.aurum.service.VoucherService;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * CustomVoucherResource controller
 */
@RestController
@RequestMapping("/api/custom-voucher")
public class CustomVoucherResource {

    private static final String ENTITY_NAME = "voucher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(CustomVoucherResource.class);
    private final CustomVoucherService customVoucherService;

    public CustomVoucherResource(CustomVoucherService customVoucherService) {
        this.customVoucherService = customVoucherService;
    }

    /**
    * POST saveVoucher
    */
    @PostMapping("/save-voucher")
    public ResponseEntity<Voucher> saveVoucher(@Valid @RequestBody Voucher voucher) throws URISyntaxException {
        log.debug("REST request to save Voucher : {}", voucher);
        if (voucher.getId() != null) {
            throw new BadRequestAlertException("A new voucher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Voucher result = customVoucherService.save(voucher);
        return ResponseEntity.created(new URI("/api/vouchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

}
