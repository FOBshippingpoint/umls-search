package com.sscs.metamaplite;

import bioc.BioCDocument;
import gov.nih.nlm.nls.metamap.document.FreeText;
import gov.nih.nlm.nls.metamap.lite.types.Entity;
import gov.nih.nlm.nls.metamap.lite.types.Ev;
import gov.nih.nlm.nls.ner.MetaMapLite;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class MetaMapLiteService {
    @Value("${metamaplite.main-directory}")
    public String metaMapLiteMainDirectory;
    private  MetaMapLite metaMapLiteInst;

    @PostConstruct
    public void init() throws IOException, ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException {
        // 或許可以用@PropertySource等取代，不過目前先這樣
        Properties properties = new Properties();
        InputStream inStream = new ClassPathResource("metamaplite.properties").getInputStream();
        properties.load(inStream);
        List<String> keys = Arrays.asList("metamaplite.index.directory", "metamaplite.models.directory", "opennlp.en-sent.bin.path", "opennlp.en-token.bin.path", "opennlp.en-pos.bin.path", "opennlp.en-chunker.bin.path", "metamaplite.excluded.termsfile");
        replacePropertiesThatContainsMainDirectory(properties, keys);
        metaMapLiteInst = new MetaMapLite(properties);
    }

    public List<String> mapFreeText(java.lang.String freeText) throws Exception {
        BioCDocument document = FreeText.instantiateBioCDocument(freeText);
        List<Entity> entityList = metaMapLiteInst.processDocument(document);

        List<String> cuis = new ArrayList<>();
        for (Entity entity : entityList) {
            for (Ev ev : entity.getEvSet()) {
                String cui = ev.getConceptInfo().getCUI();
                cuis.add(cui);
            }
        }

        return cuis;
    }

    private void replacePropertiesThatContainsMainDirectory(Properties properties, List<String> keys) {
        for (String key : keys) {
            if (properties.containsKey(key)) {
                String value = properties.getProperty(key);
                String replacedValue = value.replace("metamaplite", metaMapLiteMainDirectory);
                properties.setProperty(key, replacedValue);
            }
        }
    }
}