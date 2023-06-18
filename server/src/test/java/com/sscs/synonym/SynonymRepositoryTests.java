package com.sscs.synonym;

import com.sscs.concept.Concept;
import com.sscs.concept.ConceptRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SynonymRepositoryTests {
    @Autowired
    ConceptRepository conceptRepository;
    @Autowired
    SynonymRepository repository;

    @Test
    void findByCui() {
        var concept = conceptRepository.save(new Concept("C0948008", "Ischemic Stroke"));

        var s1 = new Synonym(concept, "Ischemic stroke", "MTH");
        var s2 = new Synonym(concept, "stroke ischemic", "CHV");
        var s3 = new Synonym(concept, "Stroke, ischemic", "OMIM");
        var s4 = new Synonym(concept, "Stroke, Ischemic", "MSH");
        var s5 = new Synonym(concept, "STROKE, ISCHEMIC", "OMIM");
        var s6 = new Synonym(concept, "Ischemic Stroke", "MSH");
        var s7 = new Synonym(concept, "ischemic stroke", "CHV");
        var s8 = new Synonym(concept, "ischaemic strokes", "CHV");
        var s9 = new Synonym(concept, "ischaemic stroke", "CHV");
        var s10 = new Synonym(concept, "ischemic strokes", "CHV");
        var s11 = new Synonym(concept, "Ischaemic Strokes", "MSH");
        var s12 = new Synonym(concept, "Ischaemic Stroke", "MSH");
        var s13 = new Synonym(concept, "Ischemic Strokes", "MSH");
        var s14 = new Synonym(concept, "Stroke, Ischaemic", "MSH");
        var s15 = new Synonym(concept, "Ischaemic stroke", "HPO");
        var s16 = new Synonym(concept, "Ischemic Cerebrovascular Accident", "NCI");

        repository.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16));

        assertThat(repository.findByConcept(concept)).hasSize(16);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}