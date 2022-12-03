package com.corp;

import com.corp.entity.Birthday;
import com.corp.entity.Company;
import com.corp.entity.User;
import com.corp.util.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 7);
        System.out.println();

        session.getTransaction().commit();
    }

    @Test
    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("lastname");

        Class<User> clazz = User.class;
        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));

    }

    @Test
    void checkReflectionApi() {

        User user = User.builder()
                .username("ivan1@gmail.com")
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