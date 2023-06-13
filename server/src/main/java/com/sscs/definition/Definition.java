package com.sscs.definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sscs.cui.Cui;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "definitions")
@Getter
@Setter // use lombok to avoid boilerplate
@NoArgsConstructor
public class Definition {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "definition_id")
	@JsonIgnore
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cui")
	private Cui cui;

	@Column(name = "meaning")
	private String meaning;

	@Column(name = "source_name")
	private String sourceName;
}
