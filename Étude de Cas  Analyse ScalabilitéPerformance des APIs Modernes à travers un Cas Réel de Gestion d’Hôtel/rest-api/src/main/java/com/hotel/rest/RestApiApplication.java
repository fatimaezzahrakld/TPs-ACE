package com.hotel.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for REST API.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hotel.common", "com.hotel.rest"})
@EntityScan(basePackages = "com.hotel.common.entity")
@EnableJpaRepositories(basePackages = "com.hotel.common.repository")
public class RestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}
