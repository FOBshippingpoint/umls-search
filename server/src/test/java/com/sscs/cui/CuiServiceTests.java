package com.sscs.cui;


import com.sscs.relationship.Relationship;
import com.sscs.relationship.RelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CuiServiceTests {
    @Autowired
    CuiService service;
    @Autowired
    CuiRepository cuiRepository;
    @Autowired
    RelationshipRepository relationshipRepository;

    Cui cui;

    @BeforeEach
    void setup() {
        cui = cuiRepository.save(new Cui("C0948008", "Ischemic Stroke", "Disease or Syndrome"));

        var b1 = new Relationship();
        b1.setCui1(cui);
        b1.setCui2(cuiRepository.save(new Cui("C2733158", "Cerebral Small Vessel Diseases", "Disease or Syndrome")));
        b1.setRelType(Relationship.RelType.BROADER);

        var b2 = new Relationship();
        b2.setCui1(cui);
        b2.setCui2(cuiRepository.save(new Cui("C0948008", "Ischemic stroke", "Disease or Syndrome")));
        b2.setRelType(Relationship.RelType.BROADER);

        var b3 = new Relationship();
        b3.setCui1(cui);
        b3.setCui2(cuiRepository.save(new Cui("C0852393", "Central nervous system haemorrhages and cerebrovascular accidents", "Disease or Syndrome")));
        b3.setRelType(Relationship.RelType.BROADER);

        var n1 = new Relationship();
        n1.setCui1(cui);
        n1.setCui2(cuiRepository.save(new Cui("C5397597", "Pontine ischemic lacunes", "Pathologic Function")));
        n1.setRelType(Relationship.RelType.NARROWER);

        relationshipRepository.saveAll(Arrays.asList(b1, b2, b3, n1));
    }

    @Test
    void findCuiByCuiString() {
        assertThat(service.findCuiByCuiString(cui.getCui())).isPresent();
    }

    @Test
    void findBroaderConcepts() {
        assertThat(service.findBroaderConceptsByCui(cui)).hasSize(3);
    }

    @Test
    void findNarrowerConcepts() {
        assertThat(service.findNarrowerConceptsByCui(cui)).hasSize(1);
    }
}