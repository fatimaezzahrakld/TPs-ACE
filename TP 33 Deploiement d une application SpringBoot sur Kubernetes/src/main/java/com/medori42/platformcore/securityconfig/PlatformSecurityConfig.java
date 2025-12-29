package com.fatimaezzahrakld.platformcore.securityconfig;

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
 * Security configuration for the Platform Core Application.
 *
 * <p>
 * Configures CORS, disables CSRF for stateless APIs, and secures endpoints.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Configuration
@EnableWebSecurity
public class PlatformSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(PlatformSecurityConfig.class);

    private static final String PUBLIC_API_PATTERN = "/api/**";
    private static final String ALL_ROUTES_PATTERN = "/**";
    private static final String ALLOW_ALL_ORIGINS = "*";
    private static final String ALLOW_ALL_HEADERS = "*";
    private static final List<String> ALLOWED_HTTP_METHODS = Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");

    /**
     * Default constructor for PlatformSecurityConfig.
     */
    public PlatformSecurityConfig() {
        // Default constructor
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http HttpSecurity builder
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        logger.info("Configuring Platform Security Filter Chain.");
        configureCors(http);
        configureCsrf(http);
        configureAuthorization(http);
        return http.build();
    }

    private void configureCors(final HttpSecurity http) throws Exception {
        logger.info("Enabling CORS for Platform Core.");
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
    }

    private void configureCsrf(final HttpSecurity http) throws Exception {
        logger.info("Disabling CSRF for Platform Core stateless APIs.");
        http.csrf(AbstractHttpConfigurer::disable);
    }

    private void configureAuthorization(final HttpSecurity http) throws Exception {
        logger.info(
                "Configuring authorization for Platform Core: permitting {} and requiring authentication for others.",
                PUBLIC_API_PATTERN);
        http.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_API_PATTERN).permitAll()
                .anyRequest().authenticated());
    }

    /**
     * Configures CORS settings for the application.
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("Creating Platform CORS configuration source.");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(ALLOW_ALL_ORIGINS));
        config.setAllowedMethods(ALLOWED_HTTP_METHODS);
        config.setAllowedHeaders(Arrays.asList(ALLOW_ALL_HEADERS));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ALL_ROUTES_PATTERN, config);
        return source;
    }
}
