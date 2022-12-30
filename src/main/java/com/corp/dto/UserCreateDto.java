package com.corp.dto;

import com.corp.entity.PersonalInfo;
import com.corp.entity.Role;

public record UserCreateDto(PersonalInfo personalInfo,
                            String userName,
                            String info,
                            Role role,
                            Integer companyId) {

}
