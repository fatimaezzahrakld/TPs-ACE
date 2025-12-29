package com.example.appareil.service;

import com.example.appareil.entity.Device;
import com.example.appareil.entity.Category;
import com.example.appareil.repository.DeviceRepository;
import com.example.appareil.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing Device operations.
 *
 * <p>
 * This class provides business logic for device management, including
 * CRUD operations and bulk state updates. It ensures devices are
 * associated with valid categories.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@Service
public class DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Saves a new device or updates an existing one.
     *
     * @param device The device to save
     * @return The saved device
     * @throws RuntimeException If the associated category is not found
     */
    public Device save(Device device) {
        logger.info("Saving device: {}", device.getLabel());
        Optional<Category> category = categoryRepository.findById(device.getCategory().getId());
        if (category.isEmpty()) {
            logger.error("Category not found for device: {}", device.getLabel());
            throw new RuntimeException("Category not found.");
        }
        device.setCategory(category.get());
        return deviceRepository.save(device);
    }

    /**
     * Retrieves a device by its identifier.
     *
     * @param id Identifier of the device
     * @return An Optional containing the device if found
     */
    public Optional<Device> findById(Long id) {
        logger.info("Fetching device with ID: {}", id);
        return deviceRepository.findById(id);
    }

    /**
     * Deletes a device by its identifier.
     *
     * @param id Identifier of the device to delete
     */
    public void deleteById(Long id) {
        logger.info("Deleting device with ID: {}", id);
        deviceRepository.deleteById(id);
    }

    /**
     * Retrieves all devices from the database.
     *
     * @return List of all devices
     */
    public List<Device> findAll() {
        logger.info("Fetching all devices from the database.");
        return deviceRepository.findAll();
    }

    /**
     * Updates the state (ON/OFF) of a specific device.
     *
     * @param id      Identifier of the device to update
     * @param devInfo Device information containing the new state
     * @throws ResourceNotFoundException If the device is not found
     */
    public void update(Long id, Device devInfo) {
        logger.info("Updating state for device ID: {}", id);
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Device not found with ID: {}", id);
                    return new ResourceNotFoundException("Device not found with id " + id);
                });
        device.setState(devInfo.isState());
        deviceRepository.save(device);
    }

    /**
     * Updates the state of all devices in the system.
     *
     * @param state The new state to apply to all devices
     */
    public void updateAll(boolean state) {
        logger.info("Updating state of all devices to: {}", state ? "ON" : "OFF");
        List<Device> deviceList = deviceRepository.findAll();
        deviceList.forEach(d -> d.setState(state));
        deviceRepository.saveAll(deviceList);
    }
}
