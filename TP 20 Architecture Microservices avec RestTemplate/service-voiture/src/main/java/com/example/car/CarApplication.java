package com.example.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Main application class for the Car Service.
 *
 * <p>
 * This microservice manages car information and uses RestTemplate to
 * communicate with the Client Service. It registers itself with the
 * Eureka discovery server.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CarApplication {

    private static final Logger logger = LoggerFactory.getLogger(CarApplication.class);

    /**
     * Main entry point for the Car Service application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class, args);
        logger.info("Car Service started successfully and registered with discovery server.");
    }

    /**
     * Configures RestTemplate for inter-service communication.
     *
     * <p>
     * This bean provides a RestTemplate instance with configured timeouts
     * to ensure robust communication between microservices.
     * </p>
     *
     * @return A configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        logger.info("Configuring RestTemplate bean with custom timeouts.");
        RestTemplate restTemplate = new RestTemplate();

        // Configuration of timeouts
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000); // 5 seconds for connection
        requestFactory.setReadTimeout(5000); // 5 seconds for reading

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
