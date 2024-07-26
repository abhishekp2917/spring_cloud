package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ZuulAPIGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulAPIGatewayApplication.class, args);
    }
}