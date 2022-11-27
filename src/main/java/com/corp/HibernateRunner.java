package com.corp;

import com.corp.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.SQLException;

import static com.corp.util.HibernateUtil.buildSessionFactory;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {

        // user Transient по отношению к любой из двух сессий
        User user = User.builder().username("ivan@gmail.com").firstname("Ivan").lastname("Ivanov").build();

        try (SessionFactory sessionFactory = buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                session1.saveOrUpdate(user); // user Persistent для session1 но Transient для session2

                session1.getTransaction().commit();
            } // Сессия закрылась и user стал Detached по отношению к session1 но все еще Transient для session2

            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                user.setFirstname("Sveta"); // Установили имя Света, но пока еще не проассоциирован в persistentContext для session2, пока он Transient
                //                session2.delete(user); // Сначала произойдет get() нашего user и он стание в состояние Persistent для session2
                session2.refresh(user); // Происходит запрос в БД и мы все изменения из БД накладываем на нашего user, что добавляет его в PersistentContext, т.к. вначале был метод get()
                session2.getTransaction()
                        .commit(); // А теперь вызовется SQL delete, после чего он станет Removed по отношении к session2
            }
        }
    }
}
