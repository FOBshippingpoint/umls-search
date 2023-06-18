package com.sscs.synonym;

import com.sscs.concept.Concept;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SynonymRepository extends CrudRepository<Synonym, Long> {
    List<Synonym> findByConcept(Concept concept);
}
