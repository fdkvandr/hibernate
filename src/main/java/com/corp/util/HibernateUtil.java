package com.corp.util;

import com.corp.entity.*;
import com.corp.listener.AuditTableListener;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        registerListeners(sessionFactory);

        return sessionFactory;

    }

    private static void registerListeners(SessionFactory sessionFactory) {
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry listenerRegistry = sessionFactoryImpl.getServiceRegistry()
                .getService(EventListenerRegistry.class);
        AuditTableListener auditTableListener = new AuditTableListener();
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(Profile.class);
        configuration.addAnnotatedClass(Chat.class);
        configuration.addAnnotatedClass(UserChat.class);
        configuration.addAnnotatedClass(Audit.class);
        //        configuration.addAnnotatedClass(Programmer.class);
        //        configuration.addAnnotatedClass(Manager.class);
        configuration.addAnnotatedClass(Payment.class);
        // configuration.addAttributeConverter(BirthdayConverter.class);
        return configuration;
    }
}
