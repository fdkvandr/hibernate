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
             Session session = sessionFactory.openSession();) {
            session.beginTransaction();

            session.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            // session.setDefaultReadOnly(true);
            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}
