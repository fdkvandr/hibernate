package com.corp.dao;

import com.corp.entity.Payment;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.corp.entity.QPayment.payment;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(entityManager, Payment.class);
    }

    public List<Payment> findAllByReceiverId(Long receiverId) {
        return new JPAQuery<Payment>(getEntityManager()).select(payment)
                .from(payment)
                .where(payment.receiver.id.eq(receiverId))
                .fetch();
    }
}
