package com.sscs.umls;

import bioc.BioCDocument;
import gov.nih.nlm.nls.metamap.document.FreeText;
import gov.nih.nlm.nls.metamap.lite.types.Entity;
import gov.nih.nlm.nls.metamap.lite.types.Ev;
import gov.nih.nlm.nls.ner.MetaMapLite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class UMLSSearchServiceImpl implements UMLSSearchService {

    @Autowired
    private UMLSTermRepository umlsTermRepository;

    @Override
    public List<UMLSTermEntity> searchDefinitionsByText(String queryText) throws Exception {
        // 使用MetaMapLite將查詢詞彙映射到CUIs
        List<String> cuis = mapQueryToCUIs(queryText);

        // 根據CUIs從數據庫中獲取UmlsTerm實體，並返回
        return umlsTermRepository.findByCuiIn(cuis);
    }

    @Override
    public List<UMLSTermEntity> searchDefinitionsByCUI(String cui) {
        // 使輸入CUI對大小寫不敏感
        cui = cui.toUpperCase();

        return umlsTermRepository.findByCui(cui);
    }


    /**
     * Set the MetaMapLite properties
     *
     * @param myProperties Properties object containing the MetaMapLite properties
     * @throws IOException if the properties file cannot be read
     */
    private void setProperties(Properties myProperties) throws IOException {
        MetaMapLite.expandModelsDir(myProperties, "metamaplite/public_mm_lite/data/models");
        MetaMapLite.expandIndexDir(myProperties, "metamaplite/public_mm_lite/data/ivf/2022AB/USAbase");
        myProperties.setProperty("metamaplite.excluded.termsfile", "metamaplite/public_mm_lite/data/specialterms.txt");
        myProperties.load(new FileReader("src/main/java/com/sscs/config/metamaplite.properties"));

    }


    private List<String> mapQueryToCUIs(String queryText) throws Exception {
        // Set MetaMapLite properties
        Properties myProperties = new Properties();
        setProperties(myProperties);

        // Instantiate MetaMapLite instance
        MetaMapLite metaMapLiteInst = new MetaMapLite(myProperties);


        // Process the input text
        BioCDocument document = FreeText.instantiateBioCDocument(queryText);

        // Set the document id to 1
        document.setID("1");

        // Set the document text
        List<BioCDocument> documentList = new ArrayList<>();
        documentList.add(document);

        // Process the document list
        List<Entity> entityList = metaMapLiteInst.processDocumentList(documentList);


        // Get the CUIs from the entity list
        List<String> cuis = new ArrayList<>();
        for (Entity entity : entityList) {
            for (Ev ev : entity.getEvSet()) {
                cuis.add(ev.getConceptInfo().getCUI());
            }
        }

        // Remove duplicate CUIs
        cuis = cuis.stream().distinct().collect(Collectors.toList());

        return cuis;
    }


    private static class SearchResult {
        private int totalCount;
        private List<UMLSTermEntity> results;

        public SearchResult(int totalCount, List<UMLSTermEntity> results) {
            this.totalCount = totalCount;
            this.results = results;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<UMLSTermEntity> getResults() {
            return results;
        }

        public void setResults(List<UMLSTermEntity> results) {
            this.results = results;
        }
    }
}
