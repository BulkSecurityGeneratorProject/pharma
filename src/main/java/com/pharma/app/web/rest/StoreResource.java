package com.pharma.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pharma.app.domain.Medecine;
import com.pharma.app.domain.Store;
import com.pharma.app.repository.MedecineRepository;
import com.pharma.app.repository.StoreRepository;
import com.pharma.app.repository.search.StoreSearchRepository;
import com.pharma.app.web.rest.errors.BadRequestAlertException;
import com.pharma.app.web.rest.util.HeaderUtil;
import com.pharma.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Store.
 */
@RestController
@RequestMapping("/api")
public class StoreResource {

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);

    private static final String ENTITY_NAME = "store";

    private final StoreRepository storeRepository;
    @Inject
    MedecineRepository medcRepository;

    private final StoreSearchRepository storeSearchRepository;

    public StoreResource(StoreRepository storeRepository, StoreSearchRepository storeSearchRepository) {
        this.storeRepository = storeRepository;
        this.storeSearchRepository = storeSearchRepository;
    }

    /**
     * POST  /stores : Create a new store.
     *
     * @param store the store to create
     * @return the ResponseEntity with status 201 (Created) and with body the new store, or with status 400 (Bad Request) if the store has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
     */
    @PostMapping("/stores")
    @Timed
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) throws URISyntaxException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        log.debug("REST request to save Store : {}", store);
        if (store.getId() != null) {
            throw new BadRequestAlertException("A new store cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Store result = storeRepository.save(store);
        storeSearchRepository.save(result);
        
        List<Medecine> listmed=medcRepository.findByStore(store);
        if(!listmed.isEmpty())
        for(Medecine medc: listmed ){
        	 System.out.println(" d1 ");	
        	medcRepository.delete(medc.getId());
        }
        Workbook workbook = new XSSFWorkbook();
        ByteArrayInputStream is = new ByteArrayInputStream(store.getDatafile());
        
        
       
        Workbook wb;
		try {
			wb = WorkbookFactory.create(is);
			 
	        Sheet sheet = wb.getSheetAt(0);
	        Iterator rowIterator = sheet.rowIterator();
	        
	        while (rowIterator.hasNext()) {
	            Row row = (Row) rowIterator.next();
	            Iterator cellIterator = row.cellIterator();
	 
	            while (cellIterator.hasNext()) {
	                Cell cell = (Cell) cellIterator.next();
 	               // System.out.print(cell.getStringCellValue());
 	                System.out.print(" : ");
	                
	                Medecine medc=new Medecine();
	               if( cell.toString().length()>2){
	                medc.setName(cell.toString());
	                medc.setStore(result);
	                medcRepository.save(medc);
	               }
	            }
	            System.out.println();
	        }
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stores : Updates an existing store.
     *
     * @param store the store to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated store,
     * or with status 400 (Bad Request) if the store is not valid,
     * or with status 500 (Internal Server Error) if the store couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException 
     */
    @PutMapping("/stores")
    @Timed
    public ResponseEntity<Store> updateStore(@Valid @RequestBody Store store) throws URISyntaxException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        log.debug("REST request to update Store : {}", store);
        if (store.getId() == null) {
            return createStore(store);
        }
        Store result = storeRepository.save(store);
        storeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, store.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stores : get all the stores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stores in body
     */
    @GetMapping("/stores")
    @Timed
    public ResponseEntity<List<Store>> getAllStores(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stores");
        Page<Store> page = storeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stores/:id : get the "id" store.
     *
     * @param id the id of the store to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the store, or with status 404 (Not Found)
     */
    @GetMapping("/stores/{id}")
    @Timed
    public ResponseEntity<Store> getStore(@PathVariable Long id) {
        log.debug("REST request to get Store : {}", id);
        Store store = storeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(store));
    }

    /**
     * DELETE  /stores/:id : delete the "id" store.
     *
     * @param id the id of the store to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stores/{id}")
    @Timed
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        log.debug("REST request to delete Store : {}", id);
        storeRepository.delete(id);
        storeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stores?query=:query : search for the store corresponding
     * to the query.
     *
     * @param query the query of the store search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stores")
    @Timed
    public ResponseEntity<List<Store>> searchStores(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Stores for query {}", query);
        Page<Store> page = storeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
