package com.jhipster.sample.web.rest;

import com.jhipster.sample.Application;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.repository.CityRepository;

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

    @Inject
    private CityRepository cityRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCityMockMvc;

    private City city;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CityResource cityResource = new CityResource();
        ReflectionTestUtils.setField(cityResource, "cityRepository", cityRepository);
        this.restCityMockMvc = MockMvcBuilders.standaloneSetup(cityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        city = new City();
        city.setName(DEFAULT_NAME);
        city.setCode(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createCity() throws Exception {
        int databaseSizeBeforeCreate = cityRepository.findAll().size();

        // Create the City

        restCityMockMvc.perform(post("/api/citys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(city)))
                .andExpect(status().isCreated());

        // Validate the City in the database
        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeCreate + 1);
        City testCity = citys.get(citys.size() - 1);
        assertThat(testCity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCity.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cityRepository.findAll().size();
        // set the field null
        city.setName(null);

        // Create the City, which fails.

        restCityMockMvc.perform(post("/api/citys")
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

        restCityMockMvc.perform(post("/api/citys")
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
        cityRepository.saveAndFlush(city);

        // Get all the citys
        restCityMockMvc.perform(get("/api/citys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

        // Get the city
        restCityMockMvc.perform(get("/api/citys/{id}", city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(city.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get("/api/citys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

		int databaseSizeBeforeUpdate = cityRepository.findAll().size();

        // Update the city
        city.setName(UPDATED_NAME);
        city.setCode(UPDATED_CODE);

        restCityMockMvc.perform(put("/api/citys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(city)))
                .andExpect(status().isOk());

        // Validate the City in the database
        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeUpdate);
        City testCity = citys.get(citys.size() - 1);
        assertThat(testCity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCity.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void deleteCity() throws Exception {
        // Initialize the database
        cityRepository.saveAndFlush(city);

		int databaseSizeBeforeDelete = cityRepository.findAll().size();

        // Get the city
        restCityMockMvc.perform(delete("/api/citys/{id}", city.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<City> citys = cityRepository.findAll();
        assertThat(citys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
