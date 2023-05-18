package com.sscs.umls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("umls")
public class UMLSSearchController {

    @Autowired
    private UMLSSearchService UMLSSearchService;


    @GetMapping("/search/text")
    public ResponseEntity<SearchResult> searchDefinitionsByText(@RequestParam String queryText) {
        try {
            System.out.println("========= Searching terms: \"" + queryText + "\" ... =========");
            List<UMLSTermEntity> result = UMLSSearchService.searchDefinitionsByText(queryText);
            System.out.println("========= Searching Done. ========");

            SearchResult searchResult = new SearchResult(result.size(), result);
            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/search/cui/{cui}")
    public ResponseEntity<SearchResult> searchDefinitionsByCUI(@PathVariable String cui) {
        try {
            System.out.println("========= Searching terms: \"" + cui + "\" ... =========");
            List<UMLSTermEntity> result = UMLSSearchService.searchDefinitionsByCUI(cui);
            System.out.println("========= Searching Done. ========");

            SearchResult searchResult = new SearchResult(result.size(), result);
            return ResponseEntity.ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }



    public static class SearchResult {
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
