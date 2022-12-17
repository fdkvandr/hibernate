package com.corp;

import com.corp.entity.User;
import com.corp.entity.UserChat;
import com.corp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.SubGraph;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory(); Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("company", "userChats");
            SubGraph<UserChat> userChats = userGraph.addSubgraph("userChats", UserChat.class);
            userChats.addAttributeNodes("chat");

            Map<String, Object> properties = Map.of(GraphSemantic.LOAD.getJakartaHintName(), userGraph);
            // session.enableFetchProfile("withCompanyAndPayments");
            User user = session.find(User.class, 1L, properties);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());

            List<User> users = session.createQuery("SELECT u FROM User u WHERE u.id = 1", User.class)
                    .setHint(GraphSemantic.LOAD.getJakartaHintName(), userGraph)
                    .list();

            users.forEach(it -> it.getCompany().getName());
            users.forEach(it -> it.getUserChats().size());

            session.getTransaction().commit();
        }
    }
}
