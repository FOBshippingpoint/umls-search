package com.sscs.relationship;

import com.sscs.concept.Concept;
import com.sscs.concept.ConceptRepository;
import com.sscs.relationship.Relationship.RelType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RelationshipRepositoryTests {
    @Autowired
    RelationshipRepository repository;
    @Autowired
    ConceptRepository conceptRepository;

    Concept rootConcept;

    @BeforeEach
    void setup() {
        rootConcept = conceptRepository.save(new Concept("C3178801", "Stroke, Lacunar"));

        var b1 = new Relationship();
        b1.setConcept1(rootConcept);
        b1.setConcept2(conceptRepository.save(new Concept("C2733158", "Cerebral Small Vessel Diseases")));
        b1.setRelType(RelType.BROADER);

        var b2 = new Relationship();
        b2.setConcept1(rootConcept);
        b2.setConcept2(conceptRepository.save(new Concept("C0948008", "Ischemic stroke")));
        b2.setRelType(RelType.BROADER);

        var b3 = new Relationship();
        b3.setConcept1(rootConcept);
        b3.setConcept2(conceptRepository.save(new Concept("C0852393", "Central nervous system haemorrhages and cerebrovascular accidents")));
        b3.setRelType(RelType.BROADER);

        var n1 = new Relationship();
        n1.setConcept1(rootConcept);
        n1.setConcept2(conceptRepository.save(new Concept("C5397597", "Pontine ischemic lacunes")));
        n1.setRelType(RelType.NARROWER);

        repository.saveAll(Arrays.asList(b1, b2, b3, n1));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void findByConcept1() {
        assertThat(repository.findByConcept1(rootConcept)).hasSize(4);
    }

    @Test
    void findByConcept2() {
        Optional<Concept> cui = conceptRepository.findByCui("C5397597");
        assertThat(cui).isPresent();
        assertThat(repository.findByConcept2(cui.get())).hasSize(1);
    }

    @Test
    void findByConcept1AndRelType() {
        assertThat(repository.findByConcept1AndRelType(rootConcept, RelType.BROADER)).hasSize(3);
    }

    @Test
    void findByConcept2AndRelType() {
        Optional<Concept> cui = conceptRepository.findByCui("C5397597");
        assertThat(cui).isPresent();
        assertThat(repository.findByConcept2AndRelType(cui.get(), RelType.NARROWER)).hasSize(1);
    }
}