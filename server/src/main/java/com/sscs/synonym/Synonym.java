package com.sscs.synonym;

import com.sscs.cui.Cui;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cui")
    private Cui cui;

    @Column(name = "term")
    private String term;

    @Column(name = "source_name")
    private String sourceName;

    public Synonym(Cui cui, String term, String sourceName) {
        this.cui = cui;
        this.term = term;
        this.sourceName = sourceName;
    }
}
