package com.corp;

import com.corp.entity.*;
import com.corp.util.HibernateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void localeInfo() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory(); var session = sessionFactory.openSession();) {
            session.beginTransaction();


            Company company = session.get(Company.class, 7L);
//            company.getLocales().add(LocaleInfo.of("ru", "Описание на русском"));
//            company.getLocales().add(LocaleInfo.of("en", "English description"));
            System.out.println(company.getLocales());

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToManySeparateEntity() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory(); var session = sessionFactory.openSession();) {
            session.beginTransaction();


            User user = session.get(User.class, 8L);
            Chat chat = session.get(Chat.class, 3L);

            UserChat userChat = UserChat.builder().createdAt(Instant.now()).createdBy(user.getUsername()).build();

            userChat.setUser(user);
            userChat.setChat(chat);

            session.persist(userChat);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOneGetUser() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory(); var session = sessionFactory.openSession();) {
            session.beginTransaction();

            User user = session.get(User.class, 17L);
            System.out.println(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory(); var session = sessionFactory.openSession();) {
            session.beginTransaction();

            User user = User.builder().username("test23@gmail.com").build();
            Profile profile = Profile.builder().language("ru").street("Kilasa 23").build();
            profile.setUser(user);
            session.persist(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrhanRemoval() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory(); var session = sessionFactory.openSession();) {
            session.beginTransaction();

            Company company = session.get(Company.class, 7);
            company.getUsers().removeIf(user -> user.getId().equals(3L));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory(); var session = sessionFactory.openSession();) {
            session.beginTransaction();

            company = session.get(Company.class, 7);
            //            company = session.getReference(Company.class, 7);

            session.getTransaction().commit();
        }
        List<User> users = company.getUsers();
        System.out.println(users.size());
    }

    @Test
    void deleteCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 9);

        session.remove(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = Company.builder().name("Facebook").build();
        var user = User.builder().username("sveta@gmail.com").build();

        // user.setCompany(company);
        // company.getUsers().add(user);
        company.addUser(user);

        session.persist(company);

        session.getTransaction().commit();
    }

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

        User user = User.builder().username("ivan1@gmail.com").build();

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