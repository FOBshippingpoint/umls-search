package com.sscs.semantictype;

import com.sscs.concept.Concept;
import com.sscs.concept.ConceptRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SemanticTypeRepositoryTests {
    @Autowired
    ConceptRepository conceptRepository;
    @Autowired
    SemanticTypeRepository repository;

    @Test
    void findByCui() {
        var cui = conceptRepository.save(new Concept("C0947722", "NRDC 149"));

        var st1 = new SemanticType(cui, "Organic Chemical");
        var st2 = new SemanticType(cui, "Hazardous or Poisonous Substance");

        repository.saveAll(Arrays.asList(st1, st2));
        assertThat(repository.findByConcept(cui)).hasSize(2);
    }
}
