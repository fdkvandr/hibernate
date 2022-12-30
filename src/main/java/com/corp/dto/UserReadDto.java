package com.corp.dto;

import com.corp.entity.PersonalInfo;
import com.corp.entity.Role;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          String info,
                          Role role,
                          CompanyReadDto company) {

}
