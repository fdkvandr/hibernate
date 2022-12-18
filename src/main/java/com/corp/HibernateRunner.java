package com.corp;

import com.corp.entity.Payment;
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
             Session session = sessionFactory.openSession();
             Session session1 = sessionFactory.openSession()) {
            session.beginTransaction();
            session1.beginTransaction();

            session.createQuery("SELECT p FROM Payment p", Payment.class)
                    .setLockMode(LockModeType.PESSIMISTIC_READ)
                    .setHint(AvailableSettings.JAKARTA_LOCK_TIMEOUT, 5000)
                    .list();

            // Payment payment = session.find(Payment.class, 1L, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            // payment.setAmount(payment.getAmount() + 10);

            Payment payment1 = session1.find(Payment.class, 1L);
            payment1.setAmount(payment1.getAmount() + 10);

            session1.getTransaction().commit();
            session.getTransaction().commit();
        }
    }
}
