package com.fatimaezzahrakld.cloudapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Web security configuration for the cloud application.
 *
 * <p>
 * Establishes Spring Security with CORS support and allows public access
 * to API endpoints. Designed for Kubernetes demonstration environments.
 * </p>
 *
 * <p>
 * Configured security features:
 * <ul>
 * <li>Custom CORS configuration</li>
 * <li>CSRF disabled for stateless REST APIs</li>
 * <li>Authorization for requests to /api/**</li>
 * <li>Authentication required for other endpoints</li>
 * </ul>
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    /**
     * Pattern for public API endpoints.
     */
    private static final String PUBLIC_API_PATTERN = "/api/**";

    /**
     * Pattern for all routes.
     */
    private static final String ALL_ROUTES_PATTERN = "/**";

    /**
     * Wildcard to allow all origins.
     */
    private static final String ALLOW_ALL_ORIGINS = "*";

    /**
     * Wildcard to allow all headers.
     */
    private static final String ALLOW_ALL_HEADERS = "*";

    /**
     * List of allowed HTTP methods.
     */
    private static final List<String> ALLOWED_HTTP_METHODS = Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");

    /**
     * Default constructor for the security configuration.
     */
    public WebSecurityConfiguration() {
        // Default constructor
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param httpSecurityBuilder The {@link HttpSecurity} to configure
     * @return The configured {@link SecurityFilterChain}
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain configureSecurityFilterChain(
            final HttpSecurity httpSecurityBuilder) throws Exception {

        logger.info("Configuring Web Security Filter Chain.");
        configureCorsSecurity(httpSecurityBuilder);
        configureCsrfProtection(httpSecurityBuilder);
        configureRequestAuthorization(httpSecurityBuilder);

        return httpSecurityBuilder.build();
    }

    /**
     * Configures CORS security on the HttpSecurity builder.
     *
     * @param httpSecurityBuilder The HTTP security builder
     * @throws Exception If an error occurs
     */
    private void configureCorsSecurity(
            final HttpSecurity httpSecurityBuilder) throws Exception {
        logger.info("Enabling CORS with custom configuration source.");
        httpSecurityBuilder.cors(corsCustomizer -> corsCustomizer.configurationSource(createCorsConfigurationSource()));
    }

    /**
     * Configures CSRF protection on the HttpSecurity builder.
     *
     * @param httpSecurityBuilder The HTTP security builder
     * @throws Exception If an error occurs
     */
    private void configureCsrfProtection(
            final HttpSecurity httpSecurityBuilder) throws Exception {
        logger.info("Disabling CSRF protection for stateless APIs.");
        httpSecurityBuilder.csrf(AbstractHttpConfigurer::disable);
    }

    /**
     * Configures request authorizations on the HttpSecurity builder.
     *
     * @param httpSecurityBuilder The HTTP security builder
     * @throws Exception If an error occurs
     */
    private void configureRequestAuthorization(
            final HttpSecurity httpSecurityBuilder) throws Exception {
        logger.info("Configuring request authorization: permitting {} and requiring authentication for others.",
                PUBLIC_API_PATTERN);
        httpSecurityBuilder.authorizeHttpRequests(authorizationConfigurer -> authorizationConfigurer
                .requestMatchers(PUBLIC_API_PATTERN).permitAll()
                .anyRequest().authenticated());
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings.
     *
     * <p>
     * Allows all origins, common HTTP methods, and all headers.
     * </p>
     *
     * @return The configured {@link CorsConfigurationSource}
     */
    @Bean
    public CorsConfigurationSource createCorsConfigurationSource() {
        logger.info("Creating CORS configuration source.");
        CorsConfiguration corsConfig = buildCorsConfiguration();

        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration(ALL_ROUTES_PATTERN, corsConfig);

        return corsConfigSource;
    }

    /**
     * Builds the CORS configuration with defined parameters.
     *
     * @return The CORS configuration
     */
    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(ALLOW_ALL_ORIGINS));
        corsConfig.setAllowedMethods(ALLOWED_HTTP_METHODS);
        corsConfig.setAllowedHeaders(Arrays.asList(ALLOW_ALL_HEADERS));
        return corsConfig;
    }
}
