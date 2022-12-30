package com.corp.service;

import com.corp.dao.UserRepository;
import com.corp.dto.UserCreateDto;
import com.corp.dto.UserReadDto;
import com.corp.entity.User;
import com.corp.mapper.Mapper;
import com.corp.mapper.UserCreateMapper;
import com.corp.mapper.UserReadMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto) {
        // validation - можно валидировать либо саму DTO, либо уже саму сущность на уровне Hibernate. Лучше использовать валидацию на уровне DTO

        // map - преобразование DTO в сущность
        User userEntity = userCreateMapper.mapFrom(userDto);
        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(GraphSemantic.LOAD.getJpaHintName(), userRepository.getEntityManager()
                .getEntityGraph("WithCompany"));
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public boolean delete(Long id) {
        var maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybeUser.isPresent();
    }
}
