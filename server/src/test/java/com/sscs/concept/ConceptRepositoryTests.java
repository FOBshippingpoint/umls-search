package com.sscs.concept;

import com.sscs.definition.Definition;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@Slf4j
class ConceptRepositoryTests {
    @Autowired
    ConceptRepository conceptRepository;

    @Test
    void findSavedCuiById() {
        var cui = conceptRepository.save(new Concept("C0948008", "Ischemic Stroke"));
        Optional<Concept> optionalCui = conceptRepository.findByCui(cui.getCui());
        assertThat(optionalCui).isPresent();
        assertThat(optionalCui.get()).isEqualTo(cui);
    }

    @Test
    void givenCuiWithDefinitions_whenFindByCui_thenReturnDefinitions() {
        var cui = new Concept("C0948008", "Ischemic Stroke");

        var def1 = new Definition();
        def1.setConcept(cui);
        def1.setSourceName("NCI");
        def1.setMeaning("An acute episode of focal cerebral, spinal, or retinal dysfunction caused by infarction of brain tissue.");

        var def2 = new Definition();
        def2.setConcept(cui);
        def2.setSourceName("HPO");
        def2.setMeaning("Acute ischemic stroke (AIS) is defined by the sudden loss of blood flow to an area of the brain with the resulting loss...");

        var def3 = new Definition();
        def3.setConcept(cui);
        def3.setSourceName("MEDLINEPLUS");
        def3.setMeaning("<p>A <a href=\"\"https://medlineplus.gov/stroke.html\"\">stroke</a> is a medical emergency. There are two types - ischemic and <a href=\"\"https://medlineplus.gov/hemorrhagicstroke.html\"\">hemorrhagic</a>....");

        var def4 = new Definition();
        def4.setConcept(cui);
        def4.setSourceName("MSH");
        def4.setMeaning("Stroke due to BRAIN ISCHEMIA resulting in interruption or reduction of blood flow to a part of the brain. When obstruction...");

        Set<Definition> definitions = new HashSet<>();
        definitions.addAll(Arrays.asList(def1, def2, def3, def4));
        cui.setDefinitions(definitions);
        conceptRepository.save(cui);

        Optional<Concept> optionalCui = conceptRepository.findByCui(cui.getCui());
        assertThat(optionalCui).isPresent();
        assertThat(optionalCui.get().getDefinitions()).hasSize(4);
    }

    @AfterEach
    void tearDown() {
        conceptRepository.deleteAll();
    }
}