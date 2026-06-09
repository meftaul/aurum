package com.meftaul.aurum.repository;
import com.meftaul.aurum.domain.Karat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Karat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KaratRepository extends JpaRepository<Karat, Long> {

}
