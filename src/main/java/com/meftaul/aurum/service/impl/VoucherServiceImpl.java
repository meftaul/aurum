package com.meftaul.aurum.service.impl;

import com.meftaul.aurum.domain.Voucher;
import com.meftaul.aurum.repository.VoucherRepository;
import com.meftaul.aurum.service.VoucherService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Voucher}.
 */
@Service
@Transactional
public class VoucherServiceImpl implements VoucherService {

    private final Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);

    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Voucher save(Voucher voucher) {
        log.debug("Request to save Voucher : {}", voucher);
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher update(Voucher voucher) {
        log.debug("Request to update Voucher : {}", voucher);
        return voucherRepository.save(voucher);
    }

    @Override
    public Optional<Voucher> partialUpdate(Voucher voucher) {
        log.debug("Request to partially update Voucher : {}", voucher);

        return voucherRepository
            .findById(voucher.getId())
            .map(existingVoucher -> {
                if (voucher.getVoucherNo() != null) {
                    existingVoucher.setVoucherNo(voucher.getVoucherNo());
                }
                if (voucher.getCustomerId() != null) {
                    existingVoucher.setCustomerId(voucher.getCustomerId());
                }
                if (voucher.getCalculatedTotalAmount() != null) {
                    existingVoucher.setCalculatedTotalAmount(voucher.getCalculatedTotalAmount());
                }
                if (voucher.getVat() != null) {
                    existingVoucher.setVat(voucher.getVat());
                }
                if (voucher.getDisountAmount() != null) {
                    existingVoucher.setDisountAmount(voucher.getDisountAmount());
                }
                if (voucher.getStatus() != null) {
                    existingVoucher.setStatus(voucher.getStatus());
                }
                if (voucher.getTotalPayableAmount() != null) {
                    existingVoucher.setTotalPayableAmount(voucher.getTotalPayableAmount());
                }
                if (voucher.getDateCreated() != null) {
                    existingVoucher.setDateCreated(voucher.getDateCreated());
                }
                if (voucher.getAddedBy() != null) {
                    existingVoucher.setAddedBy(voucher.getAddedBy());
                }
                if (voucher.getBoxNumber() != null) {
                    existingVoucher.setBoxNumber(voucher.getBoxNumber());
                }
                if (voucher.getDeliveryDate() != null) {
                    existingVoucher.setDeliveryDate(voucher.getDeliveryDate());
                }
                if (voucher.getDeliveryStatus() != null) {
                    existingVoucher.setDeliveryStatus(voucher.getDeliveryStatus());
                }

                return existingVoucher;
            })
            .map(voucherRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Voucher> findAll(Pageable pageable) {
        log.debug("Request to get all Vouchers");
        return voucherRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Voucher> findOne(Long id) {
        log.debug("Request to get Voucher : {}", id);
        return voucherRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Voucher : {}", id);
        voucherRepository.deleteById(id);
    }
}
