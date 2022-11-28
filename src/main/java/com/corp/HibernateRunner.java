package com.corp;

import com.corp.entity.Birthday;
import com.corp.entity.PersonalInfo;
import com.corp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;

import static com.corp.util.HibernateUtil.buildSessionFactory;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {

        // user Transient по отношению к любой из двух сессий
        User user = User.builder()
                .username("petr@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .lastname("Petrov")
                        .firstname("Petr")
                        .birthDate(new Birthday(LocalDate.of(2001, 1, 1)))
                        .build())
                .build();
        log.info("User entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is created: {}", transaction);

                session1.saveOrUpdate(user); // user Persistent для session1 но Transient для session2
                log.trace("User is in prsistent state: {}, session {}", user, session1);

                session1.getTransaction().commit();
            } // Сессия закрылась и user стал Detached по отношению к session1 но все еще Transient для session2
            log.warn("User is in detached state: {}, session is closed {}", user, session1);
        } catch (Exception e) {
            log.error("Exception occured", e);
            throw e;
        }
    }
}
