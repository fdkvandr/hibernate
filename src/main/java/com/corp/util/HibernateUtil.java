package com.corp.util;

import com.corp.converter.BirthdayConverter;
import com.corp.entity.*;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAnnotatedClass(Profile.class);
        configuration.addAnnotatedClass(Chat.class);
        configuration.addAnnotatedClass(UserChat.class);
//        configuration.addAnnotatedClass(Programmer.class);
//        configuration.addAnnotatedClass(Manager.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAttributeConverter(BirthdayConverter.class);
        return configuration;
    }
}
