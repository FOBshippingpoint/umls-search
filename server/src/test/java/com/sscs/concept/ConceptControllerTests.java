package com.sscs.concept;

import com.sscs.definition.Definition;
import com.sscs.relationship.Relationship;
import com.sscs.relationship.RelationshipRepository;
import com.sscs.semantictype.SemanticType;
import com.sscs.synonym.Synonym;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ConceptControllerTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ConceptController controller;
    @Autowired
    ConceptRepository conceptRepository;
    @Autowired
    RelationshipRepository relationshipRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @BeforeEach
    void setup() {
        var concept = new Concept("C0948008", "Ischemic Stroke");

        /* Definition */
        var def1 = new Definition();
        def1.setConcept(concept);
        def1.setSourceName("NCI");
        def1.setMeaning("An acute episode of focal cerebral, spinal, or retinal dysfunction caused by infarction of brain tissue.");

        var def2 = new Definition();
        def2.setConcept(concept);
        def2.setSourceName("HPO");
        def2.setMeaning("Acute ischemic stroke (AIS) is defined by the sudden loss of blood flow to an area of the brain with the resulting loss...");
        concept.getDefinitions().addAll(Arrays.asList(def1, def2));

        /* SemanticType */
        var st = new SemanticType(concept, "Disease or Syndrome");
        concept.getSemanticTypes().add(st);

        /* Synonym */
        var s1 = new Synonym(concept, "Ischemic stroke", "MTH");
        var s2 = new Synonym(concept, "stroke ischemic", "CHV");
        var s3 = new Synonym(concept, "Stroke, ischemic", "OMIM");
        concept.getSynonyms().addAll(Arrays.asList(s1, s2, s3));

        /* Relationship */
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
        conceptRepository.save(concept);
    }

    @AfterEach
    void tearDown() {
        relationshipRepository.deleteAll();
        conceptRepository.deleteAll();
    }

    @Test
    void givenCui_whenFindByCui_thenReturnCui() throws Exception {
        mockMvc.perform(get("/concepts/C0948008"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.preferredName").value("Ischemic Stroke"))
                .andExpect(jsonPath("$.definitions").isNotEmpty())
                .andExpect(jsonPath("$.synonyms").isNotEmpty())
                .andExpect(jsonPath("$.semanticTypes").isNotEmpty())
                .andExpect(jsonPath("$.broaderConcepts", hasSize(3)))
                .andExpect(jsonPath("$.broaderConcepts[0].cui").value("C2733158"))
                .andExpect(jsonPath("$.broaderConcepts[0].definition").isMap())
                .andExpect(jsonPath("$.narrowerConcepts", hasSize(1)));
    }

    @Test
    void searchCuisByFreeText() throws Exception {
        mockMvc.perform(get("/concepts?queryText=Ischemic Stroke"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenFindByCui_thenTriggerNotFound() throws Exception {
        String cui = "🤓🤓🤓";
        mockMvc.perform(get("/concepts/" + cui))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(new ConceptNotFoundException(cui).getLocalizedMessage()));
    }
}