package com.pharma.app.repository.search;

import com.pharma.app.domain.Medecine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Medecine entity.
 */
public interface MedecineSearchRepository extends ElasticsearchRepository<Medecine, Long> {
}
