package com.example.appareil.cotroller;

import com.example.appareil.entity.Device;
import com.example.appareil.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Device management.
 *
 * <p>
 * This class exposes REST endpoints for device operations. It uses
 * the DeviceService for business logic.
 * </p>
 *
 * <p>
 * Exposed Endpoints:
 * </p>
 * <ul>
 * <li>GET /api/controller/device/: Retrieves all devices</li>
 * <li>GET /api/controller/device/id/{id}: Retrieves a device by ID</li>
 * <li>POST /api/controller/device/: Adds a new device</li>
 * <li>PUT /api/controller/device/id/{id}: Updates a device state</li>
 * <li>DELETE /api/controller/device/id/{id}: Deletes a device</li>
 * <li>GET /api/controller/device/update/state/{state}: Updates state of all
 * devices</li>
 * </ul>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/api/controller/device")
@CrossOrigin
public class DeviceController {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    /**
     * Adds a new device.
     *
     * @param device The device to add
     * @return The saved device
     */
    @PostMapping("/")
    public ResponseEntity<Device> save(@RequestBody Device device) {
        logger.info("REST request to save new device: {}", device.getLabel());
        Device savedDevice = deviceService.save(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDevice);
    }

    /**
     * Retrieves a device by its ID.
     *
     * @param id Identifier of the device
     * @return The device if found
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Device> findById(@PathVariable Long id) {
        logger.info("REST request to fetch device with ID: {}", id);
        return deviceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a device by its ID.
     *
     * @param id Identifier of the device to delete
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("REST request to delete device with ID: {}", id);
        deviceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all devices.
     *
     * @return List of all devices
     */
    @GetMapping("/")
    public List<Device> findAll() {
        logger.info("REST request to fetch all devices.");
        return deviceService.findAll();
    }

    /**
     * Updates a device's state by its ID.
     *
     * @param id      Identifier of the device to update
     * @param devInfo Device information containing the new state
     */
    @PutMapping("/id/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Device devInfo) {
        logger.info("REST request to update state for device ID: {}", id);
        deviceService.update(id, devInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates the state of all devices.
     *
     * @param state The new state to apply to all devices
     */
    @GetMapping("/update/state/{state}")
    public ResponseEntity<Void> updateAll(@PathVariable boolean state) {
        logger.info("REST request to update state of all devices to: {}", state);
        deviceService.updateAll(state);
        return ResponseEntity.ok().build();
    }
}
