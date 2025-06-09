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

/**
 * Service class responsible for handling operations related to {@link UtbAuthority}.
 * <p>
 * This service interacts with the database to perform CRUD operations on authorities.
 * It uses Hibernate's {@link SessionFactory} to manage database sessions and transactions.
 * </p>
 */
@Service
public class AuthorityServices {

    @Autowired
    SessionFactory sessionFactory;

    /**
     * Finds an {@link UtbAuthority} by its name.
     * <p>
     * This method queries the database for an authority with the specified name.
     * If no authority is found, it returns {@code null}.
     * </p>
     *
     * @param authorityName the name of the authority to search for.
     * @return the {@link UtbAuthority} entity if found, otherwise {@code null}.
     */
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
        } catch (NoResultException ex) {
            authority = null;
        }
        return authority;
    }

    /**
     * Finds an {@link UtbAuthority} by its ID.
     * <p>
     * This method attempts to retrieve the authority entity from the database
     * using the provided ID. If no authority is found, it returns {@code null}.
     * </p>
     *
     * @param authorityId the ID of the authority to be retrieved.
     * @return the {@link UtbAuthority} entity if found, otherwise {@code null}.
     */
    public UtbAuthority findById(Long authorityId) {
        UtbAuthority authority;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            authority = session.get(UtbAuthority.class, authorityId);
            transaction.commit();
            session.close();
        } catch (NoResultException ex) {
            authority = null;
        }
        return authority;
    }

    /**
     * Retrieves all authorities from the database.
     * <p>
     * This method queries the database to fetch all {@link UtbAuthority} entities
     * and collects them into a {@link Set}.
     * </p>
     *
     * @return a set of all {@link UtbAuthority} entities.
     */
    public Set<UtbAuthority> getAuthorities() {
        Set<UtbAuthority> authorities = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Query to fetch all authorities from the database
        Query query = session.createQuery("FROM UtbAuthority");
        authorities = (Set<UtbAuthority>) query.getResultStream().collect(Collectors.toSet());

        transaction.commit();
        session.close();
        return authorities;
    }

    /**
     * Retrieves the IDs of all authorities from the database.
     * <p>
     * This method queries the database to fetch the IDs of all {@link UtbAuthority}
     * entities and collects them into a {@link Set}.
     * </p>
     *
     * @return a set of IDs of all authorities.
     */
    public Set<Long> getAuthoritiesId() {
        Set<Long> authoritiesId = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Query to fetch IDs of all authorities from the database
        Query query = session.createQuery("SELECT id FROM UtbAuthority", Long.class);
        authoritiesId = (Set<Long>) query.getResultStream().collect(Collectors.toSet());

        transaction.commit();
        session.close();
        return authoritiesId;
    }
}
