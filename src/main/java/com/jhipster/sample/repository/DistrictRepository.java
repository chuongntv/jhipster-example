package com.jhipster.sample.repository;

import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.domain.District;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the District entity.
 */
public interface DistrictRepository extends JpaRepository<District,Long> {
    public List<District> findByCity(City city);
    public Page<District> findByCity(City city, Pageable pageable);
    public District findByCode(String code);
    @Query("SELECT d FROM District d")
    List<District> getAll();

    @Query("SELECT d FROM District d WHERE d.id=:id")
    District getOne(@Param("id") Long id);

    @Query("SELECT d FROM District d WHERE d=:dt")
    District getOne(@Param("dt") District district);

    @Modifying
    @Query("UPDATE District d SET d=:dt WHERE d=:dt")
    Integer update(@Param("dt") District district);
}
