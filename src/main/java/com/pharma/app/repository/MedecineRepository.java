package com.pharma.app.repository;

import java.util.List;

import com.pharma.app.domain.Medecine;
import com.pharma.app.domain.Store;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Medecine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedecineRepository extends JpaRepository<Medecine, Long> {

//	List<Medecine> findByStore(Store store);

	List<Medecine> findByStore(Store store);

}
