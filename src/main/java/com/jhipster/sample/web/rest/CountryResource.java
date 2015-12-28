package com.jhipster.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.domain.District;
import com.jhipster.sample.repository.CityRepository;
import com.jhipster.sample.repository.CountryRepository;
import com.jhipster.sample.repository.DistrictRepository;
import com.jhipster.sample.web.rest.dto.CountryDTO;
import com.jhipster.sample.web.rest.dto.DataList;
import com.jhipster.sample.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api/country")
public class CountryResource {

    private final Logger log = LoggerFactory.getLogger(CountryResource.class);

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CityRepository cityRepository;

    @Inject
    private DistrictRepository districtRepository;
    /**
     * POST  /countries -> Create a new country.
     */
  /*  @RequestMapping(value = "/countries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Country> createCountry(@Valid @RequestBody Country country) throws URISyntaxException {
        log.debug("REST request to save Country : {}", country);
        if (country.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("country", "idexists", "A new country cannot already have an ID")).body(null);
        }
        Country result = countryRepository.save(country);
        return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("country", result.getId().toString()))
            .body(result);
    }*/

    /**
     * PUT  /countrys -> Updates an existing country.
     */
    @RequestMapping(value = "/save",
        method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> saveCountry(@Valid @RequestBody Country country) throws URISyntaxException {
        log.debug("REST request to update Country : {}", country);
        Country tmpCountry;
        if (country.getId() != null) {
            tmpCountry = countryRepository.findOne(country.getId());
            if(tmpCountry==null) {
                return new ResponseEntity(HttpStatus.BAD_GATEWAY);
            }
        }
        tmpCountry = countryRepository.findByCode(country.getCode());
        if(tmpCountry!=null && tmpCountry.getId() !=country.getId()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        countryRepository.save(country);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * GET  /countries -> get all the countries.
     */
    @RequestMapping(value = "/fetch",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getAllCountries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Countries");
        Page<Country> page = countryRepository.findAll(pageable);
        List<CountryDTO> lstCountries = new ArrayList<>();
        for (Country country: page.getContent()) {
            lstCountries.add(new CountryDTO(country.getId(),country.getName(),country.getCode()));
        }
        return new ResponseEntity<>(new DataList(lstCountries), HttpStatus.OK);
    }

    /**
     * GET  /fetch/:id -> get the "id" country.
     */
    @RequestMapping(value = "/fetch/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getCountry(@PathVariable Long id) {
        log.debug("REST request to get Country : {}", id);
        Country country = countryRepository.findOne(id);
        if(country==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(country,HttpStatus.OK);
    }

    /**
     * DELETE  /delete/:id -> delete the "id" country.
     */
    @RequestMapping(value = "/delete/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> deleteCountry(@PathVariable Long id) {
        log.debug("REST request to delete Country : {}", id);
        Country country = countryRepository.findOne(id);
        if(country==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<City> lstCities = cityRepository.findByCountry(country);
        for (City city: lstCities) {
            List<District> lstDistricts = districtRepository.findByCity(city);
            for(District district: lstDistricts){
                districtRepository.delete(district);
            }
            cityRepository.delete(city);
        }
        countryRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
