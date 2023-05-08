package com.sscs.umls;

import java.util.List;

public interface UMLSSearchService {

    public List<UMLSTermEntity> searchDefinitionsByText(String queryText) throws Exception;


    public List<UMLSTermEntity> searchDefinitionsByCUI(String cui);
}
