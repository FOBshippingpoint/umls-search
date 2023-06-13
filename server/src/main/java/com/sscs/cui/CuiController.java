package com.sscs.cui;

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
@RequestMapping("/cuis")
public class CuiController {

    @Autowired
    private CuiService cuiService;
    @Autowired
    private MetaMapLiteService metaMapLiteService;

    @GetMapping("/{cui}")
    public ResponseEntity<CuiDTO> findCui(@PathVariable String cui) {
        Optional<Cui> foundCui = cuiService.findCuiByCuiString(cui);

        if (foundCui.isPresent()) {
            CuiDTO cuiDTO = cui2CuiDTO(foundCui.get());
            return ResponseEntity.ok(cuiDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{freeText}")
    public ResponseEntity<List<CuiDTO>> searchCuisByFreeText(@PathVariable String freeText) {
        try {
            Set<String> cuis = metaMapLiteService.mapFreeTextToCuis(freeText);

            List<CuiDTO> cuiDTOs = new ArrayList<>();
            for (String cui : cuis) {
                Optional<Cui> foundCui = cuiService.findCuiByCuiString(cui);
                if (foundCui.isPresent()) {
                    CuiDTO cuiDTO = cui2CuiDTO(foundCui.get());
                    cuiDTOs.add(cuiDTO);
                }
            }

            return ResponseEntity.ok(cuiDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private List<String> collectCuiToCuiString(Collection<Cui> cuis) {
        List<String> result = new ArrayList<>();
        for (var c : cuis) {
            result.add(c.getCui());
        }
        return result;
    }

    private CuiDTO cui2CuiDTO(Cui cui) {
        CuiDTO cuiDTO = new CuiDTO();
        cuiDTO.setCui(cui.getCui());
        cuiDTO.setPreferredName(cui.getPreferredName());
        cuiDTO.setSemanticType(cui.getSemanticType());
        cuiDTO.setDefinitions(cui.getDefinitions());
        cuiDTO.setSynonyms(cui.getSynonyms());

        cuiDTO.setBroaderConcepts(collectCuiToCuiString(cuiService.findBroaderConceptsByCui(cui)));
        cuiDTO.setNarrowerConcepts(collectCuiToCuiString(cuiService.findNarrowerConceptsByCui(cui)));

        return cuiDTO;
    }
}

@Setter
@Getter
class CuiDTO {
    String cui;
    String preferredName;
    String semanticType;
    Collection<Definition> definitions;
    Collection<Synonym> synonyms;
    Collection<String> broaderConcepts;
    Collection<String> narrowerConcepts;
}