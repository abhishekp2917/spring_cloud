package org.example.service;

import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.example.model.UtbCategory;
import org.example.model.UtbProduct;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing `UtbCategory` entities.
 * Provides methods to find categories by ID and name using Hibernate.
 */
@Service
public class CategoryServices {

    @Autowired
    SessionFactory sessionFactory;
    // Injects the Hibernate SessionFactory to manage sessions and transactions with the database.


    /**
     * Saves a new category in the database.
     *
     * @param category The `UtbCategory` object to save.
     * @return The saved `UtbCategory` object.
     */
    public UtbCategory save(UtbCategory category) {
        Session session = sessionFactory.openSession(); // Opens a new session.
        Transaction transaction = session.beginTransaction(); // Begins a transaction.
        session.save(category); // Saves the category in the database.
        transaction.commit(); // Commits the transaction.
        session.close(); // Closes the session.
        return category;
    }

    /**
     * Finds a category by its ID.
     * Opens a Hibernate session, retrieves the category, and then closes the session.
     *
     * @param id The ID of the category to find.
     * @return The `UtbCategory` object if found, or null if not found.
     */
    public UtbCategory findByCategoryId(Long id) {
        UtbCategory category;
        try {
            Session session = sessionFactory.openSession(); // Opens a new session.
            Transaction transaction = session.beginTransaction(); // Begins a transaction.
            category = session.get(UtbCategory.class, id); // Retrieves the category by ID.
            transaction.commit(); // Commits the transaction.
            session.close(); // Closes the session.
        }
        catch (NoResultException ex) {
            category = null; // Returns null if no category is found.
        }
        return category;
    }

    /**
     * Finds a category by its name.
     * Opens a Hibernate session, executes a query to find the category by name, and then closes the session.
     *
     * @param name The name of the category to find.
     * @return The `UtbCategory` object if found, or null if not found.
     */
    public UtbCategory findByCategoryName(String name) {
        UtbCategory category;
        try {
            Session session = sessionFactory.openSession(); // Opens a new session.
            Transaction transaction = session.beginTransaction(); // Begins a transaction.
            Query query = session.createQuery("FROM UtbCategory c WHERE c.name=:name"); // Creates a query to find the category by name.
            query.setParameter("name", name); // Sets the query parameter.
            category = (UtbCategory)query.getSingleResult(); // Executes the query and retrieves the result.
            transaction.commit(); // Commits the transaction.
            session.close(); // Closes the session.
        }
        catch (NoResultException ex) {
            category = null; // Returns null if no category is found.
        }
        return category;
    }
}
