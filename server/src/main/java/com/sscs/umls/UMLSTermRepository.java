package com.sscs.umls;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface UMLSTermRepository extends JpaRepository<UMLSTermEntity, String> {

    List<UMLSTermEntity> findByCui(String cui);

    List<UMLSTermEntity> findByCuiIn(Collection<String> cuis);
}
