package com.fatimaezzahrakld.fleetmanagement.web;

import com.fatimaezzahrakld.fleetmanagement.dto.AutomobileResponseDto;
import com.fatimaezzahrakld.fleetmanagement.application.FleetBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller exposing endpoints for automobile fleet management.
 *
 * <p>
 * Provides a RESTful API for consulting automobiles with their owner
 * information.
 * Endpoints are accessible via the {@code /api/automobile} prefix.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 * @see AutomobileResponseDto
 * @see FleetBusinessService
 */
@RestController
@RequestMapping("api/automobile")
public class AutomobileRestController {

    private static final Logger logger = LoggerFactory.getLogger(AutomobileRestController.class);

    private final FleetBusinessService fleetBusinessService;

    /**
     * Constructor with business service injection.
     *
     * @param fleetBusinessService The fleet management service
     */
    @Autowired
    public AutomobileRestController(FleetBusinessService fleetBusinessService) {
        this.fleetBusinessService = fleetBusinessService;
    }

    /**
     * Retrieves the complete list of automobiles with owners.
     *
     * @return A list of enriched automobile DTOs
     */
    @GetMapping
    public List<AutomobileResponseDto> getAllAutomobiles() {
        logger.info("REST request to fetch all automobiles.");
        return fleetBusinessService.retrieveAllAutomobiles();
    }

    /**
     * Retrieves a specific automobile by its identifier.
     *
     * @param automobileIdentifier The unique identifier of the automobile
     * @return The automobile DTO with owner information
     */
    @GetMapping("/{id}")
    public AutomobileResponseDto getAutomobileById(@PathVariable("id") Long automobileIdentifier) {
        logger.info("REST request to fetch automobile with ID: {}", automobileIdentifier);
        return fleetBusinessService.retrieveAutomobileById(automobileIdentifier);
    }
}
