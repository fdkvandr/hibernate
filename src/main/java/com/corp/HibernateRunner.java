package com.corp;

import com.corp.dao.CompanyRepository;
import com.corp.dao.UserRepository;
import com.corp.dto.UserCreateDto;
import com.corp.entity.PersonalInfo;
import com.corp.entity.Role;
import com.corp.interceptor.TransactionInterceptor;
import com.corp.mapper.CompanyReadMapper;
import com.corp.mapper.UserCreateMapper;
import com.corp.mapper.UserReadMapper;
import com.corp.service.UserService;
import com.corp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {

            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class}, (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
            // session.beginTransaction();

            var companyRepository = new CompanyRepository(session);
            var userRepository = new UserRepository(session);

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var userCreateMapper = new UserCreateMapper(companyRepository);

            var transactionInterceptor = new TransactionInterceptor(sessionFactory);
            UserService userService = new ByteBuddy().subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            userService.findById(1L).ifPresent(System.out::println); // Теперь мы безопасно можем работать с нашими DTO

            UserCreateDto vanyaDto = new UserCreateDto(PersonalInfo.builder()
                    .lastname("Vanya")
                    .lastname("Ivanot")
                    .birthDate(LocalDate.now())
                    .build(), null, null, Role.USER, 1);
            userService.create(vanyaDto);

            // session.getTransaction().commit();
        }
    }
}
