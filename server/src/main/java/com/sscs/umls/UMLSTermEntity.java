package com.sscs.umls;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "umls_terms")
public class UMLSTermEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "cui")
    private String cui;

    @Column(name = "definition")
    private String definition;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
