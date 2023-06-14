package com.sscs.concept;


import com.sscs.relationship.Relationship;
import com.sscs.relationship.RelationshipRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ConceptServiceTests {
    @Autowired
    ConceptService service;
    @Autowired
    ConceptRepository conceptRepository;
    @Autowired
    RelationshipRepository relationshipRepository;

    Concept concept;

    @BeforeEach
    void setup() {
        concept = conceptRepository.save(new Concept("C0948008", "Ischemic Stroke"));

        var b1 = new Relationship();
        b1.setConcept1(concept);
        b1.setConcept2(conceptRepository.save(new Concept("C2733158", "Cerebral Small Vessel Diseases")));
        b1.setRelType(Relationship.RelType.BROADER);

        var b2 = new Relationship();
        b2.setConcept1(concept);
        b2.setConcept2(conceptRepository.save(new Concept("C0948008", "Ischemic stroke")));
        b2.setRelType(Relationship.RelType.BROADER);

        var b3 = new Relationship();
        b3.setConcept1(concept);
        b3.setConcept2(conceptRepository.save(new Concept("C0852393", "Central nervous system haemorrhages and cerebrovascular accidents")));
        b3.setRelType(Relationship.RelType.BROADER);

        var n1 = new Relationship();
        n1.setConcept1(concept);
        n1.setConcept2(conceptRepository.save(new Concept("C5397597", "Pontine ischemic lacunes")));
        n1.setRelType(Relationship.RelType.NARROWER);

        relationshipRepository.saveAll(Arrays.asList(b1, b2, b3, n1));
    }

    @AfterEach
    void tearDown() {
        relationshipRepository.deleteAll();
        conceptRepository.deleteAll();
    }

    @Test
    void findConceptByCui() {
        assertThat(service.findConceptByCui(concept.getCui())).isPresent();
    }

    @Test
    void findBroaderConcepts() {
        assertThat(service.findBroaderConceptsByCui(concept)).hasSize(3);
    }

    @Test
    void findNarrowerConcepts() {
        assertThat(service.findNarrowerConceptsByCui(concept)).hasSize(1);
    }
}