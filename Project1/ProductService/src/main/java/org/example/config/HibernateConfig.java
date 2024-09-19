package org.example.config;

import org.example.model.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import java.util.Properties;

/**
 * Configuration class for setting up Hibernate.
 *
 * This class is responsible for configuring and creating a Hibernate {@link SessionFactory} bean.
 * The {@code @Configuration} annotation indicates that this class contains Spring configuration.
 *
 * The {@code getSessionFactory} method:
 * - Loads Hibernate properties from the {@code application.properties} file.
 * - Configures Hibernate using these properties.
 * - Adds model classes ({@code UtbUser}, {@code UtbAuthority}, {@code UtbPermission}) to the Hibernate configuration.
 * - Builds and returns a {@code SessionFactory} based on the provided configuration.
 *
 * The method handles potential exceptions by:
 * - Catching {@code IOException} if the properties file cannot be loaded.
 * - Catching general {@code Exception} for any other errors during the {@code SessionFactory} creation.
 *
 * @return a {@link SessionFactory} instance configured with the specified properties and model classes
 * @throws RuntimeException if there is an error loading properties or creating the {@code SessionFactory}
 */
@Configuration
public class HibernateConfig {

    @Autowired
    Environment environment;

    private final Class<?>[] modelClasses = new Class<?>[] {
            UtbProduct.class,
            UtbCategory.class,
            UtbUser.class,
            UtbAuthority.class,
            UtbRole.class,
            UtbPermission.class
    };

    /**
     * Creates and configures a {@link SessionFactory} bean.
     *
     * This method reads Hibernate properties from the {@code application.properties} file, sets up
     * the Hibernate {@code Configuration} object, and adds annotated model classes to it. Finally,
     * it builds and returns the {@code SessionFactory}.
     *
     * @return a configured {@code SessionFactory} bean
     * @throws RuntimeException if there is an issue loading properties or creating the {@code SessionFactory}
     */
    @Bean
    public SessionFactory getSessionFactory() {
        try {
            Properties properties = new Properties();
            properties.put("hibernate.connection.driver_class", environment.getProperty("hibernate.connection.driver_class"));
            properties.put("hibernate.connection.url", environment.getProperty("hibernate.connection.url"));
            properties.put("hibernate.connection.username", environment.getProperty("hibernate.connection.username"));
            properties.put("hibernate.connection.password", environment.getProperty("hibernate.connection.password"));
            properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
            properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
            properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
            properties.put("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));

            // Create and configure the Hibernate Configuration object
            org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration().setProperties(properties);

            // Add annotated model classes to the Configuration
            for (Class<?> modelClass : modelClasses) {
                config.addAnnotatedClass(modelClass);
            }

            // Build and return the SessionFactory
            return config.buildSessionFactory();
        }
        catch (Exception ex) {
            throw new RuntimeException("Error creating SessionFactory", ex);
        }
    }
}
