package com.sscs.cui;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CuiRepository extends CrudRepository<Cui, String> {
    Optional<Cui> findByCui(String s);
}
