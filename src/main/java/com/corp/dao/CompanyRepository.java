package com.corp.dao;

import com.corp.dao.RepositoryBase;
import com.corp.entity.Company;
import org.hibernate.SessionFactory;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

    public CompanyRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Company.class);
    }
}
