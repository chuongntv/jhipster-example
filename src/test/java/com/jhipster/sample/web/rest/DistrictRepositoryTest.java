package com.jhipster.sample.web.rest;

import com.jhipster.sample.Application;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.domain.District;
import com.jhipster.sample.repository.CityRepository;
import com.jhipster.sample.repository.CountryRepository;
import com.jhipster.sample.repository.DistrictRepository;
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
public class DistrictRepositoryTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String COUNTRY_NAME = "AAAA";
    private static final String COUNTRY_CODE = "BBBB";
    private static final String CITY_NAME = "AAAA";
    private static final String CITY_CODE = "BBBB";

    @Inject
    private DistrictRepository districtRepository;

    @Inject
    private CityRepository cityRepository;

    @Inject
    private CountryRepository countryRepository;

    private District district;

    private City city;

    private Country country;

    @Before
    public void setup() {
        country = new Country();
        country.setName(COUNTRY_NAME);
        country.setCode(COUNTRY_CODE);
        countryRepository.saveAndFlush(country);

        city = new City();
        city.setName(CITY_NAME);
        city.setCode(CITY_CODE);
        city.setCountry(country);
        cityRepository.save(city);

        district = new District();
        district.setName(DEFAULT_NAME);
        district.setCode(DEFAULT_CODE);
        district.setCity(city);
    }

    @Test
    @Transactional
    public void testGetAllDistrict() throws Exception {
        districtRepository.saveAndFlush(district);
        List<District> lstDistricts = new ArrayList<District>();
        lstDistricts.add(district);
        List<District> lstDistrictsOther = districtRepository.getAll();
        assertThat(lstDistricts).isEqualTo(lstDistrictsOther);
    }

    @Test
    @Transactional
    public void testGetOneId() throws Exception {
        districtRepository.saveAndFlush(district);
        District newDistrict = districtRepository.getOne(district.getId());
        StrictAssertions.assertThat(district).isEqualToComparingFieldByField(newDistrict);
    }

    @Test
    @Transactional
    public void testGetOneObject() throws Exception {
        districtRepository.saveAndFlush(district);
        District newDistrict = districtRepository.getOne(district);
        StrictAssertions.assertThat(district).isEqualToComparingFieldByField(newDistrict);
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        districtRepository.saveAndFlush(district);
        district.setCode(UPDATED_CODE);
        district.setName(UPDATED_NAME);
        StrictAssertions.assertThat(districtRepository.update(district));
    }
}
