package com.corp.dao;

import com.corp.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public interface Repository<K extends Serializable, E extends BaseEntity<K>> {

    E save(E entity);

    void delete(K id);

    void update(E entity);

    Optional<E> findById(K id, Map<String, Object> properties);

    default Optional<E> findById(K id) {
        return findById(id, emptyMap());
    }

    List<E> findAll();
}
