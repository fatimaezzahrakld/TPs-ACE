package com.hotel.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for SOAP API.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.hotel.common", "com.hotel.soap"})
@EntityScan(basePackages = "com.hotel.common.entity")
@EnableJpaRepositories(basePackages = "com.hotel.common.repository")
public class SoapApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoapApiApplication.class, args);
    }
}
