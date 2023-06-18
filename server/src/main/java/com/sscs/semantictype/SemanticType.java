package com.sscs.semantictype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sscs.concept.Concept;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "semantic_types")
@Getter
@Setter
@NoArgsConstructor
public class SemanticType {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "semantic_type_id")
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concept")
    private Concept concept;

    @Column(name = "type", columnDefinition = "TEXT")
    private String type;

    public SemanticType(Concept concept, String type) {
        this.concept = concept;
        this.type = type;
    }

}
