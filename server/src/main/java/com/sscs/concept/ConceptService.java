package com.sscs.concept;

import com.sscs.relationship.Relationship;
import com.sscs.relationship.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConceptService {

    @Autowired
    private ConceptRepository conceptRepository;
    @Autowired
    private RelationshipRepository relationshipRepository;

    public Optional<Concept> findConceptByCui(String cui) {
        return conceptRepository.findByCui(cui);
    }

    public List<Concept> findBroaderConceptsByCui(Concept concept) {
        return findRelatedConceptsByCuiAndRelType(concept, Relationship.RelType.BROADER);
    }

    public List<Concept> findNarrowerConceptsByCui(Concept concept) {
        return findRelatedConceptsByCuiAndRelType(concept, Relationship.RelType.NARROWER);
    }

    private List<Concept> findRelatedConceptsByCuiAndRelType(Concept concept, Relationship.RelType relType) {
        List<Relationship> relationships = relationshipRepository.findByConcept1AndRelType(concept, relType);
        List<Concept> concepts = new ArrayList<>();
        for (var r: relationships) {
            concepts.add(r.getConcept2());
        }

        return concepts;
    }
}
