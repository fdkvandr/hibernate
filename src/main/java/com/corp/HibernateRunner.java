package com.corp;

import com.corp.entity.Role;
import com.corp.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {

        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            Session session = sessionFactory.openSession();

            User user = User.builder()
                    .username("ivan@gmail.com")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDate(LocalDate.of(2000, 1, 19))
                    .age(20)
                    .role(Role.ADMIN)
                    .build();

            session.beginTransaction();
            session.save(user);
            session.getTransaction()
                    .commit();
        }
    }
}
