package com.sscs.metamaplite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MetaMapLiteServiceTest {
    @Autowired
    MetaMapLiteService service;

    @Test
    void mapFreeTextToCuis() {
        List<String> cuis = service.mapFreeText("A stroke is a medical condition in which poor blood flow to the brain causes cell death.");
        assertThat(cuis).contains("C0038454", "C4266577", "C0232338", "C4745084", "C4554100", "C0007587", "C0542537", "C0006104", "C4723750", "C2700379", "C1261367", "C0032854", "C5445208");
    }
}