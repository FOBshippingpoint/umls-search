package com.sscs.relationship;

import com.sscs.cui.Cui;
import com.sscs.cui.CuiRepository;
import com.sscs.relationship.Relationship.RelType;
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
    CuiRepository cuiRepository;

    Cui rootCui;

    @BeforeEach
    void setup() {
        rootCui = cuiRepository.save(new Cui("C3178801", "Stroke, Lacunar", "Disease or Syndrome"));

        var b1 = new Relationship();
        b1.setCui1(rootCui);
        b1.setCui2(cuiRepository.save(new Cui("C2733158", "Cerebral Small Vessel Diseases", "Disease or Syndrome")));
        b1.setRelType(RelType.BROADER);

        var b2 = new Relationship();
        b2.setCui1(rootCui);
        b2.setCui2(cuiRepository.save(new Cui("C0948008", "Ischemic stroke", "Disease or Syndrome")));
        b2.setRelType(RelType.BROADER);

        var b3 = new Relationship();
        b3.setCui1(rootCui);
        b3.setCui2(cuiRepository.save(new Cui("C0852393", "Central nervous system haemorrhages and cerebrovascular accidents", "Disease or Syndrome")));
        b3.setRelType(RelType.BROADER);

        var n1 = new Relationship();
        n1.setCui1(rootCui);
        n1.setCui2(cuiRepository.save(new Cui("C5397597", "Pontine ischemic lacunes", "Pathologic Function")));
        n1.setRelType(RelType.NARROWER);

        repository.saveAll(Arrays.asList(b1, b2, b3, n1));
    }

    @Test
    void findByCui1() {
        assertThat(repository.findByCui1(rootCui)).hasSize(4);
    }

    @Test
    void findByCui2() {
        Optional<Cui> cui = cuiRepository.findByCui("C5397597");
        assertThat(cui).isPresent();
        assertThat(repository.findByCui2(cui.get())).hasSize(1);
    }

    @Test
    void findByCui1AndRelType() {
        assertThat(repository.findByCui1AndRelType(rootCui, RelType.BROADER)).hasSize(3);
    }

    @Test
    void findByCui2AndRelType() {
        Optional<Cui> cui = cuiRepository.findByCui("C5397597");
        assertThat(cui).isPresent();
        assertThat(repository.findByCui2AndRelType(cui.get(), RelType.NARROWER)).hasSize(1);
    }
}