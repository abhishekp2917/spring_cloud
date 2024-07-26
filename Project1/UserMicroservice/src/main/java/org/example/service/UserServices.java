package org.example.service;

import org.example.model.UtbUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Service
public class UserServices {

    @Autowired
    SessionFactory sessionFactory;

    public UtbUser save(UtbUser user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
        return user;
    }

    public UtbUser findByUsername(String username) {
        UtbUser user;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("FROM UtbUser u WHERE u.username=:username");
            query.setParameter("username", username);
            user = (UtbUser)query.getSingleResult();
            transaction.commit();
            session.close();
        }
        catch (NoResultException ex) {
            user = null;
        }
        return user;
    }
}
