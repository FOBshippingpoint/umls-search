package com.sscs.cui;

import com.sscs.relationship.Relationship;
import com.sscs.relationship.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CuiService {

    @Autowired
    private CuiRepository cuiRepository;
    @Autowired
    private RelationshipRepository relationshipRepository;

    public Optional<Cui> findCuiByCuiString(String cui) {
        return cuiRepository.findByCui(cui);
    }

    public List<Cui> findBroaderConceptsByCui(Cui cui) {
        return findRelatedConceptsByCuiAndRelType(cui, Relationship.RelType.BROADER);
    }

    public List<Cui> findNarrowerConceptsByCui(Cui cui) {
        return findRelatedConceptsByCuiAndRelType(cui, Relationship.RelType.NARROWER);
    }

    private List<Cui> findRelatedConceptsByCuiAndRelType(Cui cui, Relationship.RelType relType) {
        List<Relationship> relationships = relationshipRepository.findByCui1AndRelType(cui, relType);
        List<Cui> cuis = new ArrayList<>();
        for (var r: relationships) {
            cuis.add(r.getCui2());
        }

        return cuis;
    }
}
