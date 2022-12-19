package com.corp;

import com.corp.entity.Chat;
import com.corp.entity.Payment;
import com.corp.entity.User;
import com.corp.entity.UserChat;
import com.corp.util.DataImporter;
import com.corp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            DataImporter.importData(sessionFactory);
            session.beginTransaction();

            User user = session.find(User.class, 1L);
            Chat chat = Chat.builder().name("vk").build();
            session.persist(chat);

            UserChat userChat = UserChat.builder().chat(chat).user(user).build();

            session.persist(userChat);
            Payment payment = session.find(Payment.class, 1L);

            session.getTransaction().commit();
        }
    }
}
