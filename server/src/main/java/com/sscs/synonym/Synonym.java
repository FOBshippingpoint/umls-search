package com.sscs.synonym;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sscs.concept.Concept;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "synonyms")
@Getter
@Setter // use lombok to avoid boilerplate
@NoArgsConstructor
public class Synonym {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "synonym_id")
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concept")
    private Concept concept;

    @Column(name = "term", columnDefinition = "TEXT")
    private String term;

    @Column(name = "source_name")
    private String sourceName;

    public Synonym(Concept concept, String term, String sourceName) {
        this.concept = concept;
        this.term = term;
        this.sourceName = sourceName;
    }
}
