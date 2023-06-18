package com.sscs.concept;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConceptRepository extends CrudRepository<Concept, String> {
    Optional<Concept> findByCui(String s);
}
