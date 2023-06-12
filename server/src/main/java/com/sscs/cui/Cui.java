package com.sscs.cui;

import java.util.*;

import com.sscs.definition.Definition;
import com.sscs.relationship.Relationship;

import com.sscs.synonym.Synonym;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cuis")
@Getter
@Setter // use lombok to avoid boilerplate
@NoArgsConstructor
@ToString
public class Cui {
    @Id
    @Column(name = "cui")
    private String cui;

    @Column(name = "preferred_name")
    private String preferredName;

    @Column(name = "semantic_type")
    private String semanticType;

    @OneToMany(mappedBy = "cui", cascade = CascadeType.ALL, orphanRemoval = true)
    public Set<Definition> definitions = new HashSet<>();

//    @OneToMany(mappedBy = "cui1")
//    public Collection<Relationship> relationships;

    @OneToMany(mappedBy = "cui", cascade = CascadeType.ALL)
    public Collection<Synonym> synonyms;

    public Cui(String cui, String preferredName, String semanticType) {
        this.cui = cui;
        this.preferredName = preferredName;
        this.semanticType = semanticType;
    }
}
