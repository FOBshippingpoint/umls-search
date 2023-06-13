package com.sscs.cui;

import com.sscs.definition.Definition;
import com.sscs.relationship.Relationship;
import com.sscs.relationship.RelationshipRepository;
import com.sscs.synonym.Synonym;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CuiControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CuiController controller;
    @Autowired
    CuiRepository cuiRepository;
    @Autowired
    RelationshipRepository relationshipRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @BeforeEach
    void setUp() {
        var cui = new Cui("C0948008", "Ischemic Stroke", "Disease or Syndrome");

        /* Definition */
        var def1 = new Definition();
        def1.setCui(cui);
        def1.setSourceName("NCI");
        def1.setMeaning("An acute episode of focal cerebral, spinal, or retinal dysfunction caused by infarction of brain tissue.");

        var def2 = new Definition();
        def2.setCui(cui);
        def2.setSourceName("HPO");
        def2.setMeaning("Acute ischemic stroke (AIS) is defined by the sudden loss of blood flow to an area of the brain with the resulting loss...");
        cui.getDefinitions().addAll(Arrays.asList(def1, def2));

        /* Synonym */
        var s1 = new Synonym(cui, "Ischemic stroke", "MTH");
        var s2 = new Synonym(cui, "stroke ischemic", "CHV");
        var s3 = new Synonym(cui, "Stroke, ischemic", "OMIM");
        cui.getSynonyms().addAll(Arrays.asList(s1, s2, s3));

        /* Relationship */
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
        cuiRepository.save(cui);
    }

    @Test
    void givenCui_whenFindByCui_thenReturnCui() throws Exception {
        mockMvc.perform(get("/cuis/C0948008"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.preferredName").value("Ischemic Stroke"))
                .andExpect(jsonPath("$.definitions").isNotEmpty())
                .andExpect(jsonPath("$.synonyms").isNotEmpty())
                .andExpect(jsonPath("$.broaderConcepts", hasSize(3)))
                .andExpect(jsonPath("$.narrowerConcepts", hasSize(1)));
    }

    @Test
    void searchCuisByFreeText() throws Exception {
        mockMvc.perform(get("/cuis/search/Ischemic Stroke"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}