package com.jhipster.sample.repository;

import com.jhipster.sample.domain.City;

import com.jhipster.sample.domain.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the City entity.
 */
public interface CityRepository extends JpaRepository<City,Long> {
    public List<City> findByCountry(Country country);
    public Page<City> findByCountry(Country country, Pageable pageable);
    public City findByCode(String code);
}
