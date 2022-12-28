package com.corp;

import com.corp.dao.PaymentRepository;
import com.corp.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {

            var paymentRepository = new PaymentRepository(sessionFactory);
            paymentRepository.findById(1L).ifPresent(System.out::println);
        }
    }
}
