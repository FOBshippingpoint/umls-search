package com.sscs.relationship;

import com.sscs.cui.Cui;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelationshipRepository extends CrudRepository<Relationship, Long> {
    List<Relationship> findByCui1(Cui cui);

    List<Relationship> findByCui2(Cui cui);

    List<Relationship> findByCui1AndRelType(Cui cui1, Relationship.RelType relType);

    List<Relationship> findByCui2AndRelType(Cui cui2, Relationship.RelType relType);
}
