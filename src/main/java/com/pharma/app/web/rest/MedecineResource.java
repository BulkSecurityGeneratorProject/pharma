package com.pharma.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pharma.app.domain.Medecine;

import com.pharma.app.repository.MedecineRepository;
import com.pharma.app.repository.search.MedecineSearchRepository;
import com.pharma.app.web.rest.errors.BadRequestAlertException;
import com.pharma.app.web.rest.util.HeaderUtil;
import com.pharma.app.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Medecine.
 */
@RestController
@RequestMapping("/api")
public class MedecineResource {

    private final Logger log = LoggerFactory.getLogger(MedecineResource.class);

    private static final String ENTITY_NAME = "medecine";

    private final MedecineRepository medecineRepository;

    private final MedecineSearchRepository medecineSearchRepository;

    public MedecineResource(MedecineRepository medecineRepository, MedecineSearchRepository medecineSearchRepository) {
        this.medecineRepository = medecineRepository;
        this.medecineSearchRepository = medecineSearchRepository;
    }

    /**
     * POST  /medecines : Create a new medecine.
     *
     * @param medecine the medecine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medecine, or with status 400 (Bad Request) if the medecine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medecines")
    @Timed
    public ResponseEntity<Medecine> createMedecine(@Valid @RequestBody Medecine medecine) throws URISyntaxException {
        log.debug("REST request to save Medecine : {}", medecine);
        if (medecine.getId() != null) {
            throw new BadRequestAlertException("A new medecine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medecine result = medecineRepository.save(medecine);
        medecineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medecines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medecines : Updates an existing medecine.
     *
     * @param medecine the medecine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medecine,
     * or with status 400 (Bad Request) if the medecine is not valid,
     * or with status 500 (Internal Server Error) if the medecine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medecines")
    @Timed
    public ResponseEntity<Medecine> updateMedecine(@Valid @RequestBody Medecine medecine) throws URISyntaxException {
        log.debug("REST request to update Medecine : {}", medecine);
        if (medecine.getId() == null) {
            return createMedecine(medecine);
        }
        Medecine result = medecineRepository.save(medecine);
        medecineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medecine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medecines : get all the medecines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medecines in body
     */
    @GetMapping("/medecines")
    @Timed
    public ResponseEntity<List<Medecine>> getAllMedecines(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Medecines");
        Page<Medecine> page = medecineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medecines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medecines/:id : get the "id" medecine.
     *
     * @param id the id of the medecine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medecine, or with status 404 (Not Found)
     */
    @GetMapping("/medecines/{id}")
    @Timed
    public ResponseEntity<Medecine> getMedecine(@PathVariable Long id) {
        log.debug("REST request to get Medecine : {}", id);
        Medecine medecine = medecineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medecine));
    }

    /**
     * DELETE  /medecines/:id : delete the "id" medecine.
     *
     * @param id the id of the medecine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medecines/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedecine(@PathVariable Long id) {
        log.debug("REST request to delete Medecine : {}", id);
        medecineRepository.delete(id);
        medecineSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/medecines?query=:query : search for the medecine corresponding
     * to the query.
     *
     * @param query the query of the medecine search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/medecines")
    @Timed
    public ResponseEntity<List<Medecine>> searchMedecines(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Medecines for query {}", query);
        Page<Medecine> page = medecineSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/medecines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
