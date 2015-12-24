package com.jhipster.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.sample.domain.District;
import com.jhipster.sample.repository.DistrictRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing District.
 */
@RestController
@RequestMapping("/api")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);
        
    @Inject
    private DistrictRepository districtRepository;
    
    /**
     * POST  /districts -> Create a new district.
     */
    @RequestMapping(value = "/districts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> createDistrict(@Valid @RequestBody District district) throws URISyntaxException {
        log.debug("REST request to save District : {}", district);
        if (district.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("district", "idexists", "A new district cannot already have an ID")).body(null);
        }
        District result = districtRepository.save(district);
        return ResponseEntity.created(new URI("/api/districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("district", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /districts -> Updates an existing district.
     */
    @RequestMapping(value = "/districts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> updateDistrict(@Valid @RequestBody District district) throws URISyntaxException {
        log.debug("REST request to update District : {}", district);
        if (district.getId() == null) {
            return createDistrict(district);
        }
        District result = districtRepository.save(district);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("district", district.getId().toString()))
            .body(result);
    }

    /**
     * GET  /districts -> get all the districts.
     */
    @RequestMapping(value = "/districts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<District>> getAllDistricts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Districts");
        Page<District> page = districtRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /districts/:id -> get the "id" district.
     */
    @RequestMapping(value = "/districts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> getDistrict(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        District district = districtRepository.findOne(id);
        return Optional.ofNullable(district)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /districts/:id -> delete the "id" district.
     */
    @RequestMapping(value = "/districts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        districtRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("district", id.toString())).build();
    }
}
