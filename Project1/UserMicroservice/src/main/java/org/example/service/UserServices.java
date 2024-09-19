package org.example.service;

import org.example.model.UtbUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

/**
 * Service class responsible for managing user-related operations.
 * <p>
 * This service interacts with the database to perform operations like saving a user
 * and retrieving users by their username. It uses Hibernate's {@link SessionFactory}
 * to manage database sessions and transactions.
 * </p>
 */
@Service
public class UserServices {

    @Autowired
    SessionFactory sessionFactory;

    /**
     * Persists a new {@link UtbUser} entity in the database.
     * <p>
     * This method saves the provided user entity and commits the transaction
     * to ensure data integrity. It opens and closes a Hibernate session for this purpose.
     * </p>
     *
     * @param user the {@link UtbUser} entity to be saved in the database.
     * @return the saved {@link UtbUser} entity.
     */
    public UtbUser save(UtbUser user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
        return user;
    }

    /**
     * Retrieves a {@link UtbUser} entity by its username.
     * <p>
     * This method queries the database to find a user with the specified username.
     * If no user is found, it returns {@code null}. The method also manages the
     * session and transaction for proper resource handling.
     * </p>
     *
     * @param username the username of the user to retrieve.
     * @return the {@link UtbUser} matching the provided username, or {@code null} if no such user exists.
     */
    public UtbUser findByUsername(String username) {
        UtbUser user;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("FROM UtbUser u WHERE u.username=:username");
            query.setParameter("username", username);
            user = (UtbUser) query.getSingleResult();
            transaction.commit();
            session.close();
        } catch (NoResultException ex) {
            user = null;
        }
        return user;
    }
}
