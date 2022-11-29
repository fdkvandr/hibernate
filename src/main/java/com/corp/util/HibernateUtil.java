package com.corp.util;

import com.corp.converter.BirthdayConverter;
import com.corp.entity.Company;
import com.corp.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);
        configuration.addAttributeConverter(BirthdayConverter.class);

        return configuration.buildSessionFactory();
    }
}
