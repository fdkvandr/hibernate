package com.corp;

import com.corp.converter.BirthdayConverter;
import com.corp.entity.Birthday;
import com.corp.entity.Role;
import com.corp.entity.User;
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
        configuration.addAttributeConverter(BirthdayConverter.class);
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            Session session = sessionFactory.openSession();

            User user = User.builder()
                    .username("ivan9@gmail.com")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                    .role(Role.ADMIN)
                    .info("""
                          {
                            "name" : "Ivan",
                            "age" : 25
                          }
                          """)
                    .build();

            session.beginTransaction();
            User user1 = session.get(User.class, "ivan8@gmail.com");
            System.out.println(user1);
            session.getTransaction().commit();
        }
    }
}
