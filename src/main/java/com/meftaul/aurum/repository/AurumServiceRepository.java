package com.meftaul.aurum.repository;
import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Voucher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the AurumService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurumServiceRepository extends JpaRepository<AurumService, Long>, JpaSpecificationExecutor<AurumService> {
    List<AurumService> findAllByVoucher(Voucher voucher);
}
