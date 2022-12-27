package com.corp;

import com.corp.entity.Payment;
import com.corp.entity.User;
import com.corp.entity.UserChat;
import com.corp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            User user;
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.find(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                var user1 = session.find(User.class, 1L);

                List<Payment> payments = session.createQuery("SELECT p FROM Payment p WHERE p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .setCacheRegion("Queries")
                        // .setHint(QueryHints.HINT_CACHEABLE, true)
                        .getResultList();

                session.getTransaction().commit();
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                var user2 = session.find(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();
                session.get(UserChat.class, 1L);

                List<Payment> payments2 = session.createQuery("SELECT p FROM Payment p WHERE p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
                        .setCacheable(true)
                        .setCacheRegion("Queries")
                        // .setHint(QueryHints.HINT_CACHEABLE, true)
                        .getResultList();

                session.getTransaction().commit();
            }
            System.out.println(sessionFactory.getStatistics());
            System.out.println(sessionFactory.getStatistics().getCacheRegionStatistics("Users"));
        }
    }
}
