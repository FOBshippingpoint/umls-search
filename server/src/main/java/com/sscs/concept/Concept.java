package com.sscs.concept;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sscs.definition.Definition;
import com.sscs.semantictype.SemanticType;
import com.sscs.synonym.Synonym;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "concepts")
@Getter
@Setter // use lombok to avoid boilerplate
@NoArgsConstructor
@ToString
public class Concept {
    @Id
    @Column(name = "cui")
    private String cui;

    @Column(name = "preferred_name", columnDefinition = "TEXT")
    private String preferredName;

    @OneToMany(mappedBy = "concept", cascade = CascadeType.ALL)
    @JsonBackReference
    public Set<Definition> definitions = new HashSet<>();

    @OneToMany(mappedBy = "concept", cascade = CascadeType.ALL)
    @JsonBackReference
    public Set<Synonym> synonyms = new HashSet<>();

    @OneToMany(mappedBy = "concept", cascade = CascadeType.ALL)
    @JsonBackReference
    public Set<SemanticType> semanticTypes = new HashSet<>();

//    public Set<Relationship> broaderConcepts = new HashSet<>();
//    public Set<Relationship> narrowerConcepts = new HashSet<>();
//
//    public Set<Relationship> getNarrowerConcepts() {
//
//        return narrowerConcepts;
//    }

    public Concept(String cui, String preferredName) {
        this.cui = cui;
        this.preferredName = preferredName;
    }
}
