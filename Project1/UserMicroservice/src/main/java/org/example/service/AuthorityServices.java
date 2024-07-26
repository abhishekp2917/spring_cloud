package org.example.service;

import org.example.model.UtbAuthority;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityServices {

    @Autowired
    SessionFactory sessionFactory;

    public UtbAuthority findByName(String authorityName) {
        UtbAuthority authority;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("FROM UtbAuthority a WHERE a.name=:name");
            query.setParameter("name", authorityName);
            authority = (UtbAuthority) query.getSingleResult();
            transaction.commit();
            session.close();
        }
        catch (NoResultException ex) {
            authority = null;
        }
        return authority;
    }

    public UtbAuthority findById(Long authorityId) {
        UtbAuthority authority;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            authority = session.get(UtbAuthority.class, authorityId);
            transaction.commit();
            session.close();
        }
        catch (NoResultException ex) {
            authority = null;
        }
        return authority;
    }

    public Set<UtbAuthority> getAuthorities() {
        Set<UtbAuthority> authorities = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM UtbAuthority");
        authorities = (Set<UtbAuthority>) query.getResultStream().collect(Collectors.toSet());
        transaction.commit();
        session.close();
        return authorities;
    }

    public Set<Long> getAuthoritiesId() {
        Set<Long> authoritiesId = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("SELECT id FROM UtbAuthority", Long.class);
        authoritiesId = (Set<Long>) query.getResultStream().collect(Collectors.toSet());
        transaction.commit();
        session.close();
        return authoritiesId;
    }
}
