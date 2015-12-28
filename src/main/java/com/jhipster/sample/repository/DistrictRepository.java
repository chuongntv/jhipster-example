package com.jhipster.sample.repository;

import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.District;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the District entity.
 */
public interface DistrictRepository extends JpaRepository<District,Long> {
    public List<District> findByCity(City city);
}
