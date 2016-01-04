package com.jhipster.sample.web.rest;

import com.jhipster.sample.Application;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.repository.CountryRepository;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by pm01 on 1/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CountryRepositoryTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    @Inject
    private CountryRepository countryRepository;

    private Country country;

    @Before
    public void setUp() throws  Exception{
        country = new Country();
        country.setCode(DEFAULT_CODE);
        country.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void testGetAllCountry() throws Exception {
        countryRepository.saveAndFlush(country);
        List<Country> lstCountries = new ArrayList<Country>();
        lstCountries.add(country);
        List<Country> lstCountriesOther = countryRepository.getAll();
        assertThat(lstCountries).isEqualTo(lstCountriesOther);
    }

    @Test
    @Transactional
    public void testGetOneId() throws Exception {
        countryRepository.saveAndFlush(country);
        Country newCountry = countryRepository.getOne(country.getId());
        assertThat(country).isEqualToComparingFieldByField(newCountry);
    }

    @Test
    @Transactional
    public void testGetOneObject() throws Exception {
        countryRepository.saveAndFlush(country);
        Country newCountry = countryRepository.getOne(country);
        assertThat(country).isEqualToComparingFieldByField(newCountry);
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        countryRepository.saveAndFlush(country);
        country.setCode(UPDATED_CODE);
        country.setName(UPDATED_NAME);
        assertThat(countryRepository.update(country));
    }
}
