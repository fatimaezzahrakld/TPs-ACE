package com.fatimaezzahrakld.fleetmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Main application class for the Automobile Fleet Management microservice.
 *
 * <p>
 * This Spring Boot application manages vehicle information and their
 * associations with clients via inter-service communication. It communicates
 * with the Client Management service to retrieve owner information.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
public class FleetManagementApplication {

    private static final Logger logger = LoggerFactory.getLogger(FleetManagementApplication.class);

    /**
     * Connection timeout in milliseconds for HTTP calls.
     */
    private static final int HTTP_CONNECTION_TIMEOUT = 5000;

    /**
     * Read timeout in milliseconds for HTTP calls.
     */
    private static final int HTTP_READ_TIMEOUT = 5000;

    /**
     * Main entry point for the Fleet Management application.
     *
     * @param commandLineArgs Command line arguments
     */
    public static void main(String[] commandLineArgs) {
        SpringApplication.run(FleetManagementApplication.class, commandLineArgs);
        logger.info("Fleet Management Application started successfully.");
    }

    /**
     * Configures and provides a RestTemplate bean for HTTP communication.
     *
     * <p>
     * Sets connection and read timeouts for reliable inter-service communication.
     * </p>
     *
     * @return A RestTemplate instance configured with appropriate timeouts
     */
    @Bean
    public RestTemplate httpRestTemplate() {
        logger.info("Initializing RestTemplate with {}ms connect timeout and {}ms read timeout.",
                HTTP_CONNECTION_TIMEOUT, HTTP_READ_TIMEOUT);
        RestTemplate restTemplateInstance = new RestTemplate();
        SimpleClientHttpRequestFactory httpRequestConfiguration = new SimpleClientHttpRequestFactory();
        httpRequestConfiguration.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
        httpRequestConfiguration.setReadTimeout(HTTP_READ_TIMEOUT);
        restTemplateInstance.setRequestFactory(httpRequestConfiguration);
        return restTemplateInstance;
    }
}
