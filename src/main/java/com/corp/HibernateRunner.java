package com.corp;

import com.corp.entity.Company;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;

import static com.corp.util.HibernateUtil.buildSessionFactory;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Company company = Company.builder().name("Google").build();

        // user Transient по отношению к любой из двух сессий
        //        User user = User.builder()
        //                .username("petr12@gmail.com")
        //                .personalInfo(PersonalInfo.builder()
        //                        .lastname("Petrov")
        //                        .firstname("Petr")
        //                        .birthDate(new Birthday(LocalDate.of(2001, 1, 1)))
        //                        .build())
        //                .company(company)
        //                .build();
        //        log.info("User entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = buildSessionFactory(); Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();

            //            session.persist(company);
            //            session.evict(company);
            //            session.persist(user);

            //            User user1 = session.get(User.class, 1L);
            //            log.info("{}", user1);
            //            user1.getCompany().getName();

            session.getTransaction().commit();

        }
    }
}
