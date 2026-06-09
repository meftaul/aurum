package com.meftaul.aurum.repository;

import com.meftaul.aurum.domain.AurumService;
import com.meftaul.aurum.domain.Voucher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AurumService entity.
 */
@Repository
public interface AurumServiceRepository extends JpaRepository<AurumService, Long>, JpaSpecificationExecutor<AurumService> {
    List<AurumService> findAllByVoucher(Voucher voucher);

    default Optional<AurumService> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AurumService> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AurumService> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select aurumService from AurumService aurumService left join fetch aurumService.voucher",
        countQuery = "select count(aurumService) from AurumService aurumService"
    )
    Page<AurumService> findAllWithToOneRelationships(Pageable pageable);

    @Query("select aurumService from AurumService aurumService left join fetch aurumService.voucher")
    List<AurumService> findAllWithToOneRelationships();

    @Query("select aurumService from AurumService aurumService left join fetch aurumService.voucher where aurumService.id =:id")
    Optional<AurumService> findOneWithToOneRelationships(@Param("id") Long id);
}
