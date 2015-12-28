package com.jhipster.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.sample.domain.City;
import com.jhipster.sample.domain.District;
import com.jhipster.sample.repository.CityRepository;
import com.jhipster.sample.repository.DistrictRepository;
import com.jhipster.sample.web.rest.dto.DataList;
import com.jhipster.sample.web.rest.dto.DistrictDTO;
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
 * REST controller for managing District.
 */
@RestController
@RequestMapping("/api/district")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    @Inject
    private CityRepository cityRepository;

    @Inject
    private DistrictRepository districtRepository;

    /**
     * POST  /districts -> Updates an existing district.
     */
    @RequestMapping(value = "/save",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> saveDistrict(@Valid @RequestBody DistrictDTO districtDto) throws URISyntaxException {
        log.debug("REST request to update District : {}", districtDto);
        City existCity = cityRepository.findOne(districtDto.getCity().getId());
        if (existCity==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        District tmpDistrict;
        if (districtDto.getId() != null) {
            tmpDistrict = districtRepository.findOne(districtDto.getId());
            if(tmpDistrict==null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        tmpDistrict = districtRepository.findByCode(districtDto.getCode());
        if(tmpDistrict!=null && tmpDistrict.getId() !=districtDto.getId()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        District district = new District();
        district.setId(districtDto.getId());
        district.setName(districtDto.getName());
        district.setCode(districtDto.getCode());
        district.setCity(existCity);
        districtRepository.save(district);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /districts -> get all the districts.
     */
    @RequestMapping(value = "/fetch/city/{cityId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getAllDistricts(Pageable pageable, @PathVariable Long cityId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Districts");
        City city = cityRepository.findOne(cityId);
        if (city==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Page<District> page = districtRepository.findByCity(city, pageable);
        List<DistrictDTO> lstDistricts = new ArrayList<>();
        for(District district: page.getContent()){
            lstDistricts.add(new DistrictDTO(district.getId(),district.getName(),district.getCode(),null));
        }
        return new ResponseEntity<>(new DataList(lstDistricts),HttpStatus.OK);
    }

    /**
     * GET  /districts/:id -> get the "id" district.
     */
    @RequestMapping(value = "/fetch/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getDistrict(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        District district = districtRepository.findOne(id);
        if(district == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        district.setCity(null);
        return new ResponseEntity<>(district,HttpStatus.OK);
    }

    /**
     * DELETE  /districts/:id -> delete the "id" district.
     */
    @RequestMapping(value = "/delete/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> deleteDistrict(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        District district = districtRepository.findOne(id);
        if(district==null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        districtRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
