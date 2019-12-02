package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.service.CustomVoucherService;
import com.meftaul.aurum.service.VoucherService;
import com.meftaul.aurum.service.dto.CustomVoucherDto;
import com.meftaul.aurum.service.dto.TransactionDto;
import com.meftaul.aurum.service.dto.VoucherViewerDto;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

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
    public ResponseEntity<Voucher> saveVoucher(@Valid @RequestBody CustomVoucherDto voucherDto) throws URISyntaxException {
        log.debug("REST request to save Voucher : {}", voucherDto);
        Voucher voucher = voucherDto.getVoucher();
        if (voucher.getId() != null) {
            throw new BadRequestAlertException("A new voucher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Voucher result = customVoucherService.save(voucherDto);
        return ResponseEntity.created(new URI("/api/vouchers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/custom-transaction")
    public ResponseEntity<TransactionHistory> customTransaction(@Valid @RequestBody TransactionDto txnDto) {
        return ResponseEntity.ok().body(customVoucherService.saveCustomTransaction(txnDto));
    }

    @GetMapping("/{voucherNo}")
    public ResponseEntity<VoucherViewerDto> getVoucher(@PathVariable String voucherNo) {
        log.debug("REST request to get Voucher : {}", voucherNo);
        Optional<VoucherViewerDto> voucher = customVoucherService.findByVoucherNo(voucherNo);
        return ResponseUtil.wrapOrNotFound(voucher);
    }

}
