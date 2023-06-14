package com.sscs.relationship;

import com.sscs.concept.Concept;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelationshipRepository extends CrudRepository<Relationship, Long> {
    List<Relationship> findByConcept1(Concept concept);

    List<Relationship> findByConcept2(Concept concept);

    List<Relationship> findByConcept1AndRelType(Concept concept1, Relationship.RelType relType);

    List<Relationship> findByConcept2AndRelType(Concept concept2, Relationship.RelType relType);
}
