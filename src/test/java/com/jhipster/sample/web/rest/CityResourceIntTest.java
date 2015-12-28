package com.jhipster.sample.web.rest;

import com.jhipster.sample.Application;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.domain.District;
import com.jhipster.sample.repository.CityRepository;

import com.jhipster.sample.repository.CountryRepository;
import com.jhipster.sample.repository.DistrictRepository;
import io.gatling.recorder.util.Json;
import org.boon.Str;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CityResource REST controller.
 *
 * @see CityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CityResourceIntTest {

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

    @Inject
    private DistrictRepository districtRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCityMockMvc;

    private City city;

    private Country country;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CityResource cityResource = new CityResource();
        ReflectionTestUtils.setField(cityResource, "cityRepository", cityRepository);
        ReflectionTestUtils.setField(cityResource, "countryRepository", countryRepository);
        ReflectionTestUtils.setField(cityResource, "districtRepository", districtRepository);
        this.restCityMockMvc = MockMvcBuilders.standaloneSetup(cityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
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
    public void createCity() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // Create the City

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeCreate + 1);
        City testCity = citys.get(citys.size() - 1);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCity.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCity.getCountry()).isEqualTo(country);
    }

    @Test
    @Transactional
    public void createCityDupCode() throws Exception{
        cityRepository.saveAndFlush(city);

        City city1 = new City();
        city1.setName(DEFAULT_NAME);
        city1.setCode(DEFAULT_CODE);
        city1.setCountry(country);

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city1)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setName(null);

        // Create the City, which fails.

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());

        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setCode(null);

        // Create the City, which fails.

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());

        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setCountry(null);

        // Create the City, which fails.

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());

        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCitys() throws Exception {
        // Initialize the database
        cityRepository.save(city);

        // Get all the citys
        restCityMockMvc.perform(get("/api/city/fetch/country/{id}", country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*][0].id").value(hasItem(city.getId().intValue())))
            .andExpect(jsonPath("$.[*][0].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*][0].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getCity() throws Exception {
        // Initialize the database
        cityRepository.save(city);

        // Get the city
        restCityMockMvc.perform(get("/api/city/fetch/{id}", city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(city.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get("/api/city/fetch/{id}", Long.MAX_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateCity() throws Exception {
        //create new country
        Country country1 = new Country();
        country1.setName("XXX");
        country1.setCode("YYY");
        countryRepository.save(country1);

        // Initialize the database
        cityRepository.save(city);

        int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city
        city.setName(UPDATED_NAME);
        city.setCode(UPDATED_CODE);
        city.setCountry(country1);

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isOk());

        // Validate the City in the database
        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeUpdate);
        City testCity = citys.get(citys.size() - 1);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCity.getCountry().toString()).isEqualTo(country1.toString());
    }

    @Test
    @Transactional
    public void updateCityWithDupCode() throws Exception{
        // Initialize the database
        cityRepository.save(city);

        City city1 = new City();
        city1.setName(DEFAULT_NAME);
        city1.setCode("dn");
        city1.setCountry(country);
        cityRepository.save(city1);

        // Update the district
        city.setCode("dn");

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateCityWithNullCountry() throws Exception {
        // Initialize the database
        cityRepository.save(city);

        // Update the city
        city.setCountry(null);

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateCityWithNullName() throws Exception {
        // Initialize the database
        cityRepository.save(city);

        // Update the city
        city.setName(null);

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateCityWithNullCode() throws Exception {
        // Initialize the database
        cityRepository.save(city);

        // Update the city
        city.setCode(null);

        restCityMockMvc.perform(post("/api/city/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteCity() throws Exception {
        // Initialize the database
        cityRepository.save(city);

        int databaseSizeBeforeDelete = cityRepository.findAll().size();

        // Get the city
        restCityMockMvc.perform(delete("/api/city/delete/{id}", city.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        assertThat(cityRepository.findOne(city.getId())).isEqualTo(null);
    }

    @Test
    @Transactional
    public void deleteCityWithInvalidId() throws Exception {
        // Get the city
        restCityMockMvc.perform(delete("/api/city/delete/{id}", -1)
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

}
