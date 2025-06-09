package org.example.service;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.example.model.UtbRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling role-related operations.
 * <p>
 * This service provides methods for fetching roles by their name or ID, retrieving all available roles,
 * and extracting role IDs from the database. It leverages Hibernate's {@link SessionFactory} for
 * interacting with the persistence layer.
 * </p>
 * <p>
 * The service operates within transaction boundaries to ensure data integrity during database operations.
 * In case of an invalid result, such as when no role is found, it handles exceptions gracefully and returns {@code null}.
 * </p>
 */
@Service
public class RoleServices {

    @Autowired
    SessionFactory sessionFactory;

    /**
     * Retrieves a {@link UtbRole} entity by its name.
     * <p>
     * This method performs a query to find a role in the database that matches the specified role name.
     * If no matching role is found, {@code null} is returned. The method ensures that the session and
     * transaction are properly handled and closed after the operation.
     * </p>
     *
     * @param roleName the name of the role to be retrieved.
     * @return the {@link UtbRole} matching the given name, or {@code null} if no such role exists.
     */
    public UtbRole findByName(String roleName) {
        UtbRole role;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("FROM UtbRole r WHERE r.name=:name");
            query.setParameter("name", roleName);
            role = (UtbRole) query.getSingleResult();
            transaction.commit();
            session.close();
        } catch (NoResultException ex) {
            role = null;
        }
        return role;
    }

    /**
     * Retrieves a {@link UtbRole} entity by its ID.
     * <p>
     * This method fetches a role entity using its primary key (ID). If no role with the given ID is found,
     * {@code null} is returned. The session and transaction are managed to ensure proper resource handling.
     * </p>
     *
     * @param roleId the ID of the role to be retrieved.
     * @return the {@link UtbRole} with the given ID, or {@code null} if no such role exists.
     */
    public UtbRole findById(Long roleId) {
        UtbRole role;
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            role = session.get(UtbRole.class, roleId);
            transaction.commit();
            session.close();
        } catch (NoResultException ex) {
            role = null;
        }
        return role;
    }

    /**
     * Retrieves all roles available in the database.
     * <p>
     * This method performs a query to fetch all {@link UtbRole} entities from the database and collects them into a {@link Set}.
     * Each role is represented as a full entity object. The session and transaction are properly managed and closed
     * after the query execution.
     * </p>
     *
     * @return a {@link Set} of all {@link UtbRole} entities in the database.
     */
    public Set<UtbRole> getRoles() {
        Set<UtbRole> roles;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM UtbRole");
        roles = (Set<UtbRole>) query.getResultStream().collect(Collectors.toSet());
        transaction.commit();
        session.close();
        return roles;
    }

    /**
     * Retrieves the IDs of all roles in the database.
     * <p>
     * This method performs a query to select only the IDs of the roles, rather than the full entity.
     * The result is collected into a {@link Set} of Long values representing the role IDs.
     * </p>
     *
     * @return a {@link Set} of Long values representing the IDs of all roles.
     */
    public Set<Long> getRolesId() {
        Set<Long> rolesId;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("SELECT id FROM UtbRole", Long.class);
        rolesId = (Set<Long>) query.getResultStream().collect(Collectors.toSet());
        transaction.commit();
        session.close();
        return rolesId;
    }
}
