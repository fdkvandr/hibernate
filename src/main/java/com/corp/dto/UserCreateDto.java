package com.corp.dto;

import com.corp.entity.PersonalInfo;
import com.corp.entity.Role;
import com.corp.validation.UpdateCheck;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserCreateDto(
        @Valid
        PersonalInfo personalInfo,
        @NotNull
        String userName,
        String info,
        @NotNull(groups = UpdateCheck.class)
        Role role,
        // @ValidCompany тут могли бы написать свой валидатор для такой аннотации
        Integer companyId) {

}
