package com.sscs.cui;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sscs.definition.Definition;
import com.sscs.synonym.Synonym;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cuis")
@Getter
@Setter // use lombok to avoid boilerplate
@NoArgsConstructor
@ToString
@JsonInclude
public class Cui {
    @Id
    @Column(name = "cui")
    private String cui;

    @Column(name = "preferred_name")
    private String preferredName;

    @Column(name = "semantic_type")
    private String semanticType;

    @OneToMany(mappedBy = "cui", cascade = CascadeType.ALL)
    @JsonBackReference
    public Set<Definition> definitions = new HashSet<>();

    @OneToMany(mappedBy = "cui", cascade = CascadeType.ALL)
    @JsonBackReference
    public Set<Synonym> synonyms = new HashSet<>();

//    public Set<Relationship> broaderConcepts = new HashSet<>();
//    public Set<Relationship> narrowerConcepts = new HashSet<>();
//
//    public Set<Relationship> getNarrowerConcepts() {
//
//        return narrowerConcepts;
//    }

    public Cui(String cui, String preferredName, String semanticType) {
        this.cui = cui;
        this.preferredName = preferredName;
        this.semanticType = semanticType;
    }
}
