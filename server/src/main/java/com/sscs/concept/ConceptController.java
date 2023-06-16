package com.sscs.concept;

import com.sscs.definition.Definition;
import com.sscs.metamaplite.MetaMapLiteService;
import com.sscs.synonym.Synonym;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("concepts")
public class ConceptController {

    @Autowired
    private ConceptService conceptService;
    @Autowired
    private MetaMapLiteService metaMapLiteService;

    @GetMapping("/{cui}")
    @ResponseStatus(HttpStatus.OK)
    public ConceptDTO findConceptByCui(@PathVariable String cui) {
        Concept foundCui = conceptService.findConceptByCui(cui).orElseThrow(() -> new ConceptNotFoundException(cui));
        ConceptDTO conceptDTO = concept2ConceptDTO(foundCui);
        return conceptDTO;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ConceptDTO> searchConceptsByFreeText(@RequestParam(defaultValue = "") String queryText) {
        List<String> cuis = metaMapLiteService.mapFreeText(queryText);
        List<ConceptDTO> conceptDTOs = new ArrayList<>();
        for (String cui : cuis) {
            Concept foundCui = conceptService.findConceptByCui(cui).orElseThrow(() -> new ConceptNotFoundException(cui));
            ConceptDTO conceptDTO = concept2ConceptDTO(foundCui);
            conceptDTOs.add(conceptDTO);
        }

        return conceptDTOs;
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
        for (var st : concept.getSemanticTypes()) {
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