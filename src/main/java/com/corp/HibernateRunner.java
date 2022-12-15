package com.corp;

import com.corp.entity.User;
import com.corp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // User user = session.get(User.class, 1L);
            // System.out.println(user.getPayments().size());
            // System.out.println(user.getCompany().getName());
            List<User> users = session.createQuery("select u from User u", User.class).list();

            users.get(0).getPayments().size();
            // users.forEach(user -> user.getPayments().size());
            // users.forEach(user -> user.getCompany().getName());

            session.getTransaction().commit();
        }
    }
}
