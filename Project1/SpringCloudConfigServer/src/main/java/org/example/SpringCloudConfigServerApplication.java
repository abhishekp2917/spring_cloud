package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * The @EnableConfigServer annotation in Spring Cloud is used to designate a Spring Boot application as a Config Server.
 * This server acts as a centralized configuration server, serving configuration properties to client applications.
 *
 * Config Servers provide a central place to manage and update application configurations, allowing applications to
 * retrieve configuration properties from a common source. This approach is beneficial in distributed systems where
 * configuration management needs to be consistent and centralized.
 *
 * When the application starts, it will serve configuration properties from various sources such as Git, filesystem, or
 * a database, depending on the configuration specified in application properties or YAML files.
 */
@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }
}
