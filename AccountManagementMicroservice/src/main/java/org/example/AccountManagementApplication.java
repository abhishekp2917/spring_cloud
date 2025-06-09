package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The @EnableDiscoveryClient annotation in Spring Cloud is used to register a service
 * with a discovery server, such as Netflix Eureka, Consul, or Zookeeper.
 * This annotation enables the application to act as a Discovery Client, allowing it to
 * both register itself with a discovery server and discover other services registered on that server.
 *
 * When a service starts up, it registers itself with the discovery server. This allows the
 * service to be discovered by other services in the system. The registration process typically
 * involves sending metadata such as the service ID, hostname, IP address, port, and health check URL
 * to the discovery server.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AccountManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountManagementApplication.class, args);
    }
}
