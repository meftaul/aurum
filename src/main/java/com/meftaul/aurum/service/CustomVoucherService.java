package com.meftaul.aurum.service;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.AurumServiceRepository;
import com.meftaul.aurum.repository.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomVoucherService {

    private final Logger log = LoggerFactory.getLogger(CustomVoucherService.class);

    private final VoucherRepository voucherRepository;
    private final AurumServiceRepository aurumServiceRepository;

    public CustomVoucherService(VoucherRepository voucherRepository, AurumServiceRepository aurumServiceRepository) {
        this.voucherRepository = voucherRepository;
        this.aurumServiceRepository = aurumServiceRepository;
    }

    public Voucher save(Voucher voucher) {
        log.debug("Request to save Voucher : {}", voucher);

        Voucher savedVoucher = voucherRepository.save(voucher);

        for (AurumService s : voucher.getAurumServices()) {
            s.setVoucher(savedVoucher);
            aurumServiceRepository.save(s);
            savedVoucher.addAurumService(s);
        }


        return voucherRepository.save(savedVoucher);
    }

}
