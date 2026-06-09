package com.meftaul.aurum.repository;

import com.meftaul.aurum.domain.AurumService;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AurumService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AurumServiceRepository extends JpaRepository<AurumService, Long>, JpaSpecificationExecutor<AurumService> {
}
