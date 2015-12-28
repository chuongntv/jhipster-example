package com.jhipster.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.Country;
import com.jhipster.sample.domain.District;
import com.jhipster.sample.repository.CityRepository;
import com.jhipster.sample.repository.CountryRepository;
import com.jhipster.sample.repository.DistrictRepository;
import com.jhipster.sample.web.rest.dto.CityDTO;
import com.jhipster.sample.web.rest.dto.DataList;
import com.jhipster.sample.web.rest.util.HeaderUtil;
import com.jhipster.sample.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing City.
 */
@RestController
@RequestMapping("/api/city")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CityRepository cityRepository;

    @Inject
    private DistrictRepository districtRepository;

    /**
     * POST  /citys -> Updates an existing city.
     */
    @RequestMapping(value = "/save",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> saveCity(@Valid @RequestBody CityDTO cityDto) throws URISyntaxException {
        log.debug("REST request to update City : {}", cityDto.getCode());
        Country existCountry = countryRepository.findOne(cityDto.getCountry().getId());
        if(existCountry==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        City tmpCity;
        if (cityDto.getId() != null) {
            tmpCity = cityRepository.findOne(cityDto.getId());
            if(tmpCity==null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tmpCity = cityRepository.findByCode(cityDto.getCode());
        if(tmpCity!=null && tmpCity.getId() !=cityDto.getId()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        City city = new City();
        city.setId(cityDto.getId());
        city.setName(cityDto.getName());
        city.setCode(cityDto.getCode());
        city.setCountry(existCountry);
        cityRepository.save(city);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /fetch -> get all the citys.
     */
    @RequestMapping(value = "/fetch/country/{countryId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getAllCities(Pageable pageable, @PathVariable Long countryId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cities");
        Country country = countryRepository.findOne(countryId);
        if(country==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Page<City> page = cityRepository.findByCountry(country, pageable);
        List<CityDTO> lstCities = new ArrayList<>();
        for(City city: page.getContent()){
            lstCities.add(new CityDTO(city.getId(),city.getName(),city.getCode(),null));
        }
        return new ResponseEntity<>(new DataList(lstCities),HttpStatus.OK);
    }

    /**
     * GET  /citys/:id -> get the "id" city.
     */
    @RequestMapping(value = "/fetch/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getCity(@PathVariable Long id) {
        log.debug("REST request to get City : {}", id);
        City city = cityRepository.findOne(id);
        if(city==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        city.setCountry(null);
        return new ResponseEntity<>(city,HttpStatus.OK);
    }

    /**
     * DELETE  /cities/:id -> delete the "id" city.
     */
    @RequestMapping(value = "/delete/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        log.debug("REST request to delete City : {}", id);
        City city = cityRepository.findOne(id);
        if(city==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<District> lstDistricts = districtRepository.findByCity(city);
        for(District district: lstDistricts){
            districtRepository.delete(district);
        }
        cityRepository.delete(city);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
