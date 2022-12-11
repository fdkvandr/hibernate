package com.corp.dao;

import com.corp.dto.CompanyDto;
import com.corp.entity.*;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.criteria.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
        // return session.createQuery("SELECT u FROM User u", User.class).list();
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);

        JpaRoot<User> user = criteria.from(User.class);
        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstname) {
        // return session.createQuery("SELECT u FROM User u WHERE u.personalInfo.firstname = :firstname", User.class)
        //         .setParameter("firstname", firstname)
        //         .list();

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);

        JpaRoot<User> user = criteria.from(User.class);
        criteria.where(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstname));
        criteria.select(user);

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возврастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        // return session.createQuery("SELECT u FROM User u ORDER BY u.personalInfo.birthDate", User.class)
        //         .setMaxResults(limit)
        //         // .setFirstResult(offset)
        //         .list();

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);

        JpaRoot<User> user = criteria.from(User.class);
        criteria.select(user).orderBy(cb.asc(user.get(User_.personalInfo).get("birthDate")));

        return session.createQuery(criteria).setMaxResults(limit).list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        // return session.createQuery("SELECT u FROM Company c JOIN c.users u WHERE c.name = :companyName", User.class)
        //         .setParameter("companyName", companyName)
        //         .list();

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);

        JpaRoot<Company> company = criteria.from(Company.class);
        MapJoin<Company, String, User> users = company.join(Company_.users);
        criteria.select(users).where(cb.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанным именем,
     * упорядочненные по имени сотрудников, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        // return session.createQuery("SELECT p FROM Payment p JOIN p.receiver u JOIN u.company c WHERE c.name = :companyName ORDER BY u.personalInfo.firstname ASC, p.amount ASC", Payment.class)
        //         .setParameter("companyName", companyName)
        //         .list();

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);

        JpaRoot<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);
        Join<User, Company> company = user.join(User_.company);
        criteria.select(payment)
                .where(
                        cb.equal(company.get(Company_.name), companyName)
                )
                .orderBy(
                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(payment.get(Payment_.amount))
                );

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанным именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
        // return session.createQuery("SELECT AVG(p.amount) FROM Payment p JOIN p.receiver u WHERE u.personalInfo.firstname = :firstName AND u.personalInfo.lastname = :lastName", Double.class)
        //         .setParameter("firstName", firstName)
        //         .setParameter("lastName", lastName)
        //         .uniqueResult();

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Double> criteria = cb.createQuery(Double.class);

        JpaRoot<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);

        List<Predicate> predicates = new ArrayList<>();
        if (firstName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
        }
        if (lastName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
        }

        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
                predicates.toArray(Predicate[]::new)
        );

        return session.createQuery(criteria).uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех ее сотрудников.
     * Компании упорядочены по названию
     */
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        // return session.createQuery("SELECT c.name, AVG(p.amount) from Company c JOIN c.users u JOIN u.payments p GROUP BY c.name ORDER BY c.name", Object[].class)
        //         .list();

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<CompanyDto> criteria = cb.createQuery(CompanyDto.class);

        JpaRoot<Company> company = criteria.from(Company.class);
        MapJoin<Company, String, User> user = company.join(Company_.users);
        ListJoin<User, Payment> payment = user.join(User_.payments, JoinType.INNER);


        criteria.select(
                cb.construct(CompanyDto.class, List.of(
                        company.get(Company_.name),
                        cb.avg(payment.get(Payment_.amount))))
        )
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников,
     * чей средний размер выплат больше средного размера выплат всех сотрудников.
     * Упорядочить по имени сотрудников
     */
    public List<Tuple> isItPossible(Session session) {
        // return session.createQuery("SELECT u, AVG(p.amount) AS avg_amount FROM User u JOIN u.payments p GROUP BY u HAVING AVG(p.amount) > (SELECT AVG(p.amount) FROM Payment p) ORDER BY u.personalInfo.firstname", Object[].class)
        //         .list();

        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);

        JpaRoot<User> user = criteria.from(User.class);
        ListJoin<User, Payment> payments = user.join(User_.payments);

        JpaSubQuery<Double> subquery = criteria.subquery(Double.class);
        JpaRoot<Payment> paymentSubquery = subquery.from(Payment.class);

        criteria.select(
                cb.tuple(List.of(
                    user,
                    cb.avg(payments.get(Payment_.amount)))
                )
        )
                .groupBy(user.get(User_.id))
                .having(cb.gt(
                        cb.avg(payments.get(Payment_.amount)),
                        subquery.select(cb.avg(paymentSubquery.get(Payment_.amount))))
                )
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));
        return session.createQuery(criteria).list();
    }
}
