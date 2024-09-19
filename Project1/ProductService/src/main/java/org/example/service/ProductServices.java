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
import java.util.List;

/**
 * Service class for managing `UtbProduct` entities.
 * Provides methods to save, update, and retrieve products from the database using Hibernate.
 */
@Service
public class ProductServices {

    @Autowired
    SessionFactory sessionFactory;
    // Injects the Hibernate SessionFactory to manage sessions and transactions with the database.

    /**
     * Saves a new product in the database.
     * Associates the product with its category and then saves it.
     *
     * @param product The `UtbProduct` object to save.
     * @return The saved `UtbProduct` object.
     */
    public UtbProduct save(UtbProduct product) {
        Session session = sessionFactory.openSession(); // Opens a new session.
        Transaction transaction = session.beginTransaction(); // Begins a transaction.
        UtbCategory category = session.get(UtbCategory.class, product.getCategory().getId());
        // Retrieves the category associated with the product by its ID.
        product.setCategory(category); // Sets the category to the product.
        session.save(product); // Saves the product in the database.
        transaction.commit(); // Commits the transaction.
        session.close(); // Closes the session.
        return product;
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The `UtbProduct` object with updated information.
     * @return The updated `UtbProduct` object.
     */
    public UtbProduct update(UtbProduct product) {
        Session session = sessionFactory.openSession(); // Opens a new session.
        Transaction transaction = session.beginTransaction(); // Begins a transaction.
        session.update(product); // Updates the product in the database.
        transaction.commit(); // Commits the transaction.
        session.close(); // Closes the session.
        return product;
    }

    /**
     * Finds a product by its ID.
     * Opens a Hibernate session, retrieves the product by its ID, and then closes the session.
     *
     * @param id The ID of the product to find.
     * @return The `UtbProduct` object if found, or null if not found.
     */
    public UtbProduct findByProductId(Long id) {
        UtbProduct product;
        try {
            Session session = sessionFactory.openSession(); // Opens a new session.
            Transaction transaction = session.beginTransaction(); // Begins a transaction.
            product = session.get(UtbProduct.class, id); // Retrieves the product by ID.
            transaction.commit(); // Commits the transaction.
            session.close(); // Closes the session.
        }
        catch (NoResultException ex) {
            product = null; // Returns null if no product is found.
        }
        return product;
    }

    /**
     * Finds a product by its name.
     * Opens a Hibernate session, executes a query to find the product by name, and then closes the session.
     *
     * @param name The name of the product to find.
     * @return The `UtbProduct` object if found, or null if not found.
     */
    public UtbProduct findByProductName(String name) {
        UtbProduct product;
        try {
            Session session = sessionFactory.openSession(); // Opens a new session.
            Transaction transaction = session.beginTransaction(); // Begins a transaction.
            Query query = session.createQuery("FROM UtbProduct p WHERE p.name=:name"); // Creates a query to find the product by name.
            query.setParameter("name", name); // Sets the query parameter.
            product = (UtbProduct) query.getSingleResult(); // Executes the query and retrieves the result.
            transaction.commit(); // Commits the transaction.
            session.close(); // Closes the session.
        }
        catch (NoResultException ex) {
            product = null; // Returns null if no product is found.
        }
        return product;
    }

    /**
     * Finds products by their category name.
     * First, retrieves the category by its name, and then finds all products associated with that category.
     *
     * @param categoryName The name of the category.
     * @return A list of `UtbProduct` objects, or null if no products are found.
     */
    public List<UtbProduct> findByProductCategory(String categoryName) {
        List<UtbProduct> products;
        try {
            Session session = sessionFactory.openSession(); // Opens a new session.
            Transaction transaction = session.beginTransaction(); // Begins a transaction.
            Query query1 = session.createQuery("FROM UtbCategory c WHERE c.name=:name", UtbCategory.class);
            // Query to find the category by name.
            query1.setParameter("name", categoryName); // Sets the query parameter.
            UtbCategory category = (UtbCategory) query1.getSingleResult(); // Retrieves the category by name.
            Query query2 = session.createQuery("FROM UtbProduct p WHERE p.category=:category", UtbProduct.class);
            // Query to find products by the category.
            query2.setParameter("category", category); // Sets the query parameter.
            products = query2.getResultList(); // Retrieves the list of products.
            transaction.commit(); // Commits the transaction.
            session.close(); // Closes the session.
        }
        catch (NoResultException ex) {
            products = null; // Returns null if no products are found.
        }
        return products;
    }
}
