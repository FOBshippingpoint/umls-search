package com.sscs.definition;

import com.sscs.concept.Concept;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DefinitionRepository extends CrudRepository<Definition, Long> {
   List<Definition> findByConcept(Concept concept);
}
