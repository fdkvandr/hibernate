package com.corp.mapper;

import com.corp.dto.CompanyReadDto;
import com.corp.entity.Company;
import org.hibernate.Hibernate;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {

    @Override
    public CompanyReadDto mapFrom(Company object) {
        Hibernate.initialize(object.getLocales());
        return new CompanyReadDto(object.getId(), object.getName(), object.getLocales());
    }
}
