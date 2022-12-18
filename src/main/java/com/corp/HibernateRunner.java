package com.corp;

import com.corp.entity.Payment;
import com.corp.entity.Profile;
import com.corp.entity.User;
import com.corp.util.DataImporter;
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
            DataImporter.importData(sessionFactory);
            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1L);

            session.getTransaction().commit();
        }
    }
}
