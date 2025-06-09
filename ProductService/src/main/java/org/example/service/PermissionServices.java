package org.example.service;

import org.example.model.UtbPermission;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.Query;
import java.util.List;

/**
 * Service class responsible for handling permission-related operations.
 * <p>
 * This service interacts with the database to retrieve permissions associated
 * with a specific service. It uses Hibernate's {@link SessionFactory} to
 * manage database sessions and transactions.
 * </p>
 */
@Service
public class PermissionServices {

    @Autowired
    SessionFactory sessionFactory;

    /**
     * Retrieves a list of {@link UtbPermission} entities based on the provided service name.
     * <p>
     * This method queries the database for permissions that match the specified service.
     * It also initializes the collection of authorities for each permission using
     * Hibernate's {@link Hibernate#initialize} to ensure lazy-loaded relationships are
     * properly fetched before the session is closed.
     * </p>
     *
     * @param service the name of the service for which permissions are to be fetched.
     * @return a list of {@link UtbPermission} entities related to the specified service.
     */
    public List<UtbPermission> getPermissions(String service) {
        List<UtbPermission> permissions = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Query the database for permissions matching the specified service
        Query query = session.createQuery("FROM UtbPermission p WHERE p.service = :service");
        query.setParameter("service", service);
        permissions = query.getResultList();

        // Initialize lazy-loaded relationships (authorities)
        for (UtbPermission permission : permissions) {
            Hibernate.initialize(permission.getAuthorities());
        }

        transaction.commit();
        session.close();
        return permissions;
    }
}
