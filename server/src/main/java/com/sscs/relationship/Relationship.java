package com.sscs.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sscs.concept.Concept;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "relationships")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Relationship {
	public static enum RelType {
		BROADER, NARROWER
	};

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "relationship_id")
	@JsonIgnore
	private Long id;

	@ManyToOne
	@JoinColumn(name = "concept1")
	private Concept concept1;

	@ManyToOne
	@JoinColumn(name = "concept2")
	private Concept concept2;

	@Enumerated(EnumType.STRING)
	@Column(name = "rel_type")
	RelType relType;
}
