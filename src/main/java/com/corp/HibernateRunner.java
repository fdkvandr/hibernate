package com.corp;

import com.corp.entity.Payment;
import com.corp.entity.Profile;
import com.corp.entity.User;
import com.corp.util.HibernateUtil;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;

import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            Profile profile = Profile.builder()
                    .user(session.find(User.class, 1L))
                    .language("ru")
                    .street("Kolosa 28")
                    .build();
            session.save(profile);
        }
    }
}
