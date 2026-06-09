package com.meftaul.aurum.web.rest;

import com.meftaul.aurum.domain.TransactionHistory;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.security.AuthoritiesConstants;
import com.meftaul.aurum.service.CustomVoucherService;
import com.meftaul.aurum.service.dto.CustomVoucherDto;
import com.meftaul.aurum.service.dto.TransactionDto;
import com.meftaul.aurum.service.dto.VoucherViewerDto;
import com.meftaul.aurum.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
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
    private final VoucherRepository voucherRepository;

    public CustomVoucherResource(CustomVoucherService customVoucherService, VoucherRepository voucherRepository) {
        this.customVoucherService = customVoucherService;
        this.voucherRepository = voucherRepository;
    }

    /**
    * POST saveVoucher
    */
    @PostMapping("/save-voucher")
    public ResponseEntity<Voucher> saveVoucher(@Valid @RequestBody CustomVoucherDto voucherDto) throws Exception {
        log.debug("REST request to save Voucher : {}", voucherDto);
        Voucher voucher = voucherDto.getVoucher();
        if (voucher.getId() != null) {
            throw new BadRequestAlertException("A new voucher cannot already have an ID", ENTITY_NAME, "id exists");
        }

        if (voucherDto.getPaidAmount().compareTo(voucherDto.getVoucher().getTotalPayableAmount()) == 1) {
            throw new BadRequestAlertException("Paid amount can not be greater than payable amount", ENTITY_NAME, "idexists");
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

    @Secured({AuthoritiesConstants.ADMIN})
    @DeleteMapping("/delete/{voucherNo}")
    public ResponseEntity<String> deleteVoucher(@PathVariable String voucherNo) {
        log.debug("REST request to get Voucher : {}", voucherNo);
        String vNo = customVoucherService.deleteVoucher(voucherNo);
        return ResponseEntity.accepted().body(voucherNo);
    }

}
