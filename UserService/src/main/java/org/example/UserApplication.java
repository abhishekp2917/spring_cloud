package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The @EnableDiscoveryClient annotation is used in Spring Cloud to enable service registration
 * with a discovery server, such as Netflix Eureka, Consul, or Zookeeper. This annotation allows the application
 * to act as a Discovery Client, enabling it to register itself with a discovery server and discover other services
 * that are registered on that server.
 *
 * When the service starts, it registers itself with the discovery server by sending metadata including the
 * service ID, hostname, IP address, port, and health check URL. This registration allows the service to be
 * discovered by other services in the system.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
