package com.jhipster.sample.web.rest;

import com.jhipster.sample.Application;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.repository.CityRepository;
import com.jhipster.sample.repository.CountryRepository;
import org.assertj.core.api.StrictAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pm01 on 1/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CityRepositoryTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String COUNTRY_NAME = "AAAA";
    private static final String COUNTRY_CODE = "BBBB";
    @Inject
    private CityRepository cityRepository;

    @Inject
    private CountryRepository countryRepository;
    private City city;
    private Country country;

    @Before
    public void setup() {
        country = new Country();
        country.setName(COUNTRY_NAME);
        country.setCode(COUNTRY_CODE);
        countryRepository.saveAndFlush(country);
        city = new City();
        city.setName(DEFAULT_NAME);
        city.setCode(DEFAULT_CODE);
        city.setCountry(country);
    }

    @Test
    @Transactional
    public void testGetAllCity() throws Exception {
        cityRepository.saveAndFlush(city);
        List<City> lstCities = new ArrayList<City>();
        lstCities.add(city);
        List<City> lstCitiesOther = cityRepository.getAll();
        assertThat(lstCities).isEqualTo(lstCitiesOther);
    }

    @Test
    @Transactional
    public void testGetOneId() throws Exception {
        cityRepository.saveAndFlush(city);
        City newCity = cityRepository.getOne(city.getId());
        StrictAssertions.assertThat(city).isEqualToComparingFieldByField(newCity);
    }

    @Test
    @Transactional
    public void testGetOneObject() throws Exception {
        cityRepository.saveAndFlush(city);
        City newCity = cityRepository.getOne(city);
        StrictAssertions.assertThat(city).isEqualToComparingFieldByField(newCity);
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        cityRepository.saveAndFlush(city);
        city.setCode(UPDATED_CODE);
        city.setName(UPDATED_NAME);
        StrictAssertions.assertThat(cityRepository.update(city));
    }

}
