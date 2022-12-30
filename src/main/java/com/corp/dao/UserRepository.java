package com.corp.dao;

import com.corp.entity.User;
import jakarta.persistence.EntityManager;

public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(EntityManager entityManager) {

        super(entityManager, User.class);
    }

    // TODO
}
