package com.sscs.definition;

import com.sscs.cui.Cui;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DefinitionRepository extends CrudRepository<Definition, Long> {
   List<Definition> findByCui(Cui cui);
}
