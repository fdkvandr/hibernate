package com.corp.mapper;

import com.corp.dto.UserReadDto;
import com.corp.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto mapFrom(User object) {
        return new UserReadDto(object.getId(),
                object.getPersonalInfo(),
                object.getUsername(),
                object.getInfo(),
                object.getRole(),
                Optional.ofNullable(object.getCompany()).map(companyReadMapper::mapFrom).orElse(null) // Если у пользователя не будет компании, то выдастся NullPointerException. Поэтому если у нас есть NOT NULL constraint - все будет ок, а если нет, то нужно делать либо nullSafe наш CompanyReadMapper, либо просто обернуть в Optional
        );
    }
}
