package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * The @EnableEurekaServer annotation turns your Spring Boot application into a Eureka server,
 * which provides a central registry where microservices can register themselves.
 * This registry is used by other services to discover and interact with each other.
 * This allows client applications to discover available instances of services dynamically without hardcoding service URLs,
 * supporting more flexible and scalable microservice architectures.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaDiscoveryApplication.class, args);
    }
}
