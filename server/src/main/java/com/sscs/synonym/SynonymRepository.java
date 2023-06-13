package com.sscs.synonym;

import com.sscs.cui.Cui;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SynonymRepository extends CrudRepository<Synonym, Long> {
    List<Synonym> findByCui(Cui cui);
}
