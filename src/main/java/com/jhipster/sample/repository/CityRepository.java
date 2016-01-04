package com.jhipster.sample.repository;

import com.jhipster.sample.domain.City;

import com.jhipster.sample.domain.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the City entity.
 */
public interface CityRepository extends JpaRepository<City,Long> {
    public List<City> findByCountry(Country country);
    public Page<City> findByCountry(Country country, Pageable pageable);
    public City findByCode(String code);

    @Query("SELECT c FROM City c")
    List<City> getAll();

    @Query("SELECT c FROM City c WHERE c.id=:id")
    City getOne(@Param("id") Long id);

    @Query("SELECT c FROM City c WHERE c=:ct")
    City getOne(@Param("ct") City city);

    @Modifying
    @Query("UPDATE City c SET c=:ct WHERE c=:ct")
    Integer update(@Param("ct") City city);
}
