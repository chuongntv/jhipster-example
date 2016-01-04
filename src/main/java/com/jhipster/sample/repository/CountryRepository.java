package com.jhipster.sample.repository;

import com.jhipster.sample.domain.Country;

import org.springframework.data.annotation.Persistent;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Country entity.
 */
public interface CountryRepository extends JpaRepository<Country,Long> {
    public Country findByCode(String code);

    @Query("SELECT c FROM Country c")
    List<Country> getAll();

    @Query("SELECT c FROM Country c WHERE c.id=:id")
    Country getOne(@Param("id") Long id);

    @Query("SELECT c FROM Country c WHERE c=:ct")
    Country getOne(@Param("ct") Country country);

    @Modifying
    @Query("UPDATE Country c SET c=:ct WHERE c=:ct")
    Integer update(@Param("ct") Country country);


}
