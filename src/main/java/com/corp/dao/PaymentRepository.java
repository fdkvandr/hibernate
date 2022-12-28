package com.corp.dao;

import com.corp.entity.Payment;
import org.hibernate.SessionFactory;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Payment.class);
    }
}
