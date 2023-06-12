package com.sscs.cui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sscs.definition.Definition;
import com.sscs.definition.DefinitionRepository;
import com.sscs.relationship.RelationshipRepository;
import com.sscs.synonym.SynonymRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Transactional
@SpringBootTest
@Slf4j
class CuiRepositoryTests {
    @Autowired
    CuiRepository cuiRepository;
    @Autowired
    DefinitionRepository definitionRepository;
    @Autowired
    RelationshipRepository relationshipRepository;
    @Autowired
    SynonymRepository synonymRepository;

//    @BeforeEach
//    void setup() {
//        var cui = new Cui("C0948008", "Ischemic Stroke", "Disease or Syndrome");
//        cuiRepository.save(cui);
//    }

    @Test
    void findSavedCuiById() {
        var cui = cuiRepository.save(new Cui("C0948008", "Ischemic Stroke", "Disease or Syndrome"));
        assertThat(cuiRepository.findByCui(cui.getCui())).hasValue(cui);
    }

    @Test
    void givenCuiWithDefinitions_whenFindByCui_thenReturnDefinitions() {
        var cui = new Cui("C0948008", "Ischemic Stroke", "Disease or Syndrome");

        var def1 = new Definition();
        def1.setCui(cui);
        def1.setSourceName("NCI");
        def1.setMeaning("An acute episode of focal cerebral, spinal, or retinal dysfunction caused by infarction of brain tissue.");

        var def2 = new Definition();
        def2.setCui(cui);
        def2.setSourceName("HPO");
        def2.setMeaning("Acute ischemic stroke (AIS) is defined by the sudden loss of blood flow to an area of the brain with the resulting loss...");

        var def3 = new Definition();
        def3.setCui(cui);
        def3.setSourceName("MEDLINEPLUS");
        def3.setMeaning("<p>A <a href=\"\"https://medlineplus.gov/stroke.html\"\">stroke</a> is a medical emergency. There are two types - ischemic and <a href=\"\"https://medlineplus.gov/hemorrhagicstroke.html\"\">hemorrhagic</a>....");

        var def4 = new Definition();
        def4.setCui(cui);
        def4.setSourceName("MSH");
        def4.setMeaning("Stroke due to BRAIN ISCHEMIA resulting in interruption or reduction of blood flow to a part of the brain. When obstruction...");

        Set<Definition> definitions = new HashSet<>();
        definitions.addAll(Arrays.asList(def1, def2, def3, def4));
        cui.setDefinitions(definitions);
        cuiRepository.save(cui);

        assertThat(definitionRepository.findByCui(cui)).hasSize(4);
    }
}