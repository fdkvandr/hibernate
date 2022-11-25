package com.corp;

import com.corp.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkReflectionApi() {

        User user = User.builder()
                .username("ivan1@gmail.com")
                .firstname("Ivan")
                .lastname("Ivanov")
                .birthDate(LocalDate.of(2000, 1, 19))
                .age(20)
                .build();

        String sql = """
                     insert into %s (%s) values (%s)
                     """;
        String tableName = ofNullable(user.getClass()
                .getAnnotation(Table.class)).map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class)).map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(","));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(","));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        Connection connection = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues))) {
            for (int i = 1; i <= declaredFields.length; i++) {
                declaredFields[i].setAccessible(true);
                try {
                    preparedStatement.setObject(i, declaredFields[i].get(user));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}