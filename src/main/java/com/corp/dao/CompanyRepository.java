package com.corp.dao;

import com.corp.entity.Company;
import jakarta.persistence.EntityManager;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

    public CompanyRepository(EntityManager entityManager) {
        super(entityManager, Company.class);
    }
}
