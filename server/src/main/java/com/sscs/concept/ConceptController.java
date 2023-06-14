package com.sscs.concept;

import com.sscs.definition.Definition;
import com.sscs.metamaplite.MetaMapLiteService;
import com.sscs.synonym.Synonym;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("cuis")
public class ConceptController {

    @Autowired
    private ConceptService conceptService;
    @Autowired
    private MetaMapLiteService metaMapLiteService;

    @GetMapping("/{cui}")
    public ResponseEntity<ConceptDTO> findConceptByCui(@PathVariable String cui) {
        Optional<Concept> foundCui = conceptService.findConceptByCui(cui);

        if (foundCui.isPresent()) {
            ConceptDTO conceptDTO = concept2ConceptDTO(foundCui.get());
            return ResponseEntity.ok(conceptDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{freeText}")
    public ResponseEntity<List<ConceptDTO>> searchConceptsByFreeText(@PathVariable String freeText) {
        try {
            Set<String> cuis = metaMapLiteService.mapFreeTextToCuis(freeText);

            List<ConceptDTO> conceptDTOs = new ArrayList<>();
            for (String cui : cuis) {
                Optional<Concept> foundCui = conceptService.findConceptByCui(cui);
                if (foundCui.isPresent()) {
                    ConceptDTO conceptDTO = concept2ConceptDTO(foundCui.get());
                    conceptDTOs.add(conceptDTO);
                }
            }

            return ResponseEntity.ok(conceptDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private List<String> collectConceptToCui(Collection<Concept> concepts) {
        List<String> result = new ArrayList<>();
        for (var c : concepts) {
            result.add(c.getCui());
        }
        return result;
    }

    private ConceptDTO concept2ConceptDTO(Concept concept) {
        ConceptDTO conceptDTO = new ConceptDTO();
        conceptDTO.setCui(concept.getCui());
        conceptDTO.setPreferredName(concept.getPreferredName());
        conceptDTO.setDefinitions(concept.getDefinitions());
        conceptDTO.setSynonyms(concept.getSynonyms());

        conceptDTO.setBroaderConcepts(collectConceptToCui(conceptService.findBroaderConceptsByCui(concept)));
        conceptDTO.setNarrowerConcepts(collectConceptToCui(conceptService.findNarrowerConceptsByCui(concept)));

        Set<String> semanticTypes = new HashSet<>();
        for (var st: concept.getSemanticTypes()) {
            semanticTypes.add(st.getType());
        }
        conceptDTO.setSemanticTypes(semanticTypes);

        return conceptDTO;
    }
}

@Setter
@Getter
class ConceptDTO {
    String cui;
    String preferredName;
    Collection<Definition> definitions;
    Collection<Synonym> synonyms;
    Collection<String> semanticTypes;
    Collection<String> broaderConcepts;
    Collection<String> narrowerConcepts;
}