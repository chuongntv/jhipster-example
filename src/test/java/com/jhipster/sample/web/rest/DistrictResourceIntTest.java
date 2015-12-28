package com.jhipster.sample.web.rest;

import com.jhipster.sample.Application;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.domain.District;
import com.jhipster.sample.repository.CityRepository;
import com.jhipster.sample.repository.CountryRepository;
import com.jhipster.sample.repository.DistrictRepository;

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
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DistrictResource REST controller.
 *
 * @see DistrictResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DistrictResourceIntTest {

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

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDistrictMockMvc;

    private District district;

    private City city;

    private Country country;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DistrictResource districtResource = new DistrictResource();
        ReflectionTestUtils.setField(districtResource, "districtRepository", districtRepository);
        ReflectionTestUtils.setField(districtResource, "cityRepository", cityRepository);
        this.restDistrictMockMvc = MockMvcBuilders.standaloneSetup(districtResource)
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
    public void createDistrict() throws Exception {
        int databaseSizeBeforeCreate = districtRepository.findAll().size();

        // Create the District

        restDistrictMockMvc.perform(post("/api/district/save")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(district)))
                .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeCreate + 1);
        District testDistrict = districts.get(districts.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDistrict.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDistrict.getCity()).isEqualTo(city);
    }

    @Test
    @Transactional
    public void createDistrictDupCode() throws Exception{
        districtRepository.saveAndFlush(district);

        District district1 = new District();
        district1.setName(DEFAULT_NAME);
        district1.setCode(DEFAULT_CODE);
        district1.setCity(city);

        restDistrictMockMvc.perform(post("/api/district/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(district1)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setName(null);

        // Create the District, which fails.

        restDistrictMockMvc.perform(post("/api/district/save")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(district)))
                .andExpect(status().isBadRequest());

        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setCode(null);

        // Create the District, which fails.

        restDistrictMockMvc.perform(post("/api/district/save")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(district)))
                .andExpect(status().isBadRequest());

        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtRepository.findAll().size();
        // set the field null
        district.setCity(null);

        // Create the District, which fails.

        restDistrictMockMvc.perform(post("/api/district/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(district)))
            .andExpect(status().isBadRequest());

        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDistricts() throws Exception {
        // Initialize the database
        districtRepository.save(district);

        // Get all the districts
        restDistrictMockMvc.perform(get("/api/district/fetch/city/{id}", city.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*][0].id").value(hasItem(district.getId().intValue())))
                .andExpect(jsonPath("$.[*][0].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*][0].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getDistrict() throws Exception {
        // Initialize the database
        districtRepository.save(district);

        // Get the district
        restDistrictMockMvc.perform(get("/api/district/fetch/{id}", district.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(district.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDistrict() throws Exception {
        // Get the district
        restDistrictMockMvc.perform(get("/api/district/fetch/{id}", Long.MAX_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateDistrict() throws Exception {
        // Initialize the database
        districtRepository.save(district);

		int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district
        district.setName(UPDATED_NAME);
        district.setCode(UPDATED_CODE);

        restDistrictMockMvc.perform(post("/api/district/save")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(district)))
                .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districts.get(districts.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDistrict.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDistrict.getCity()).isEqualTo(city);
    }

    @Test
    @Transactional
    public void updateDistrictWithDupCode() throws Exception{
        // Initialize the database
        districtRepository.save(district);

        District district1 = new District();
        district1.setName(DEFAULT_NAME);
        district1.setCode("vi");
        district1.setCity(city);
        districtRepository.save(district1);

        // Update the district
        district.setCode("vi");

        restDistrictMockMvc.perform(post("/api/district/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(district)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateDistrictWithNullName() throws Exception {
        // Initialize the database
        districtRepository.save(district);

        // Update the district
        district.setName(null);

        restDistrictMockMvc.perform(post("/api/district/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateDistrictWithNullCode() throws Exception {
        // Initialize the database
        districtRepository.save(district);

        // Update the district
        district.setCode(null);

        restDistrictMockMvc.perform(post("/api/district/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateDistrictWithNullCity() throws Exception {
        // Initialize the database
        districtRepository.save(district);

        // Update the district
        district.setCity(null);

        restDistrictMockMvc.perform(post("/api/district/save")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(city)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteDistrict() throws Exception {
        // Initialize the database
        districtRepository.save(district);

        // Get the district
        restDistrictMockMvc.perform(delete("/api/district/delete/{id}", district.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        assertThat(districtRepository.findOne(district.getId())).isEqualTo(null);
    }

    @Test
    @Transactional
    public void deleteDistrictWithInvalidId() throws Exception {
        // Delete the district
        restDistrictMockMvc.perform(delete("/api/district/delete/{id}", -1)
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }
}
