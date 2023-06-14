package com.sscs.semantictype;

import com.sscs.concept.Concept;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SemanticTypeRepository extends CrudRepository<SemanticType, Long> {
   List<SemanticType> findByConcept(Concept concept);
}
