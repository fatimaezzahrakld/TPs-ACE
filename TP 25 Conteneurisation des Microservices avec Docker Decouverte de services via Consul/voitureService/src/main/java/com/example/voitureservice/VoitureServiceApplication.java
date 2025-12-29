package com.example.voitureservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Main application class for the Car Service.
 *
 * <p>
 * This microservice manages car information and registers itself
 * with Consul for service discovery. It includes a simple REST controller
 * and a JPA entity for demonstration purposes.
 * </p>
 *
 * @author Kelladi fatimaezzahra
 * @version 1.0.0
 * @since 2025-12-29
 */
@SpringBootApplication
@RestController
public class VoitureServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(VoitureServiceApplication.class);

	@Autowired
	private CarRepository carRepository;

	/**
	 * Main entry point for the Car Service application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(VoitureServiceApplication.class, args);
		logger.info("Car Service started successfully.");
	}

	/**
	 * Home endpoint for service health check or simple greeting.
	 *
	 * @return A greeting message
	 */
	@GetMapping("/")
	public String home() {
		logger.info("Home endpoint accessed.");
		return "Hello from Car Service";
	}

	/**
	 * Retrieves all cars from the database.
	 *
	 * @return List of all cars
	 */
	@GetMapping("/voitures")
	public List<Car> getAllCars() {
		logger.info("Fetching all cars from the database.");
		return carRepository.findAll();
	}
}

/**
 * JPA Entity representing a Car.
 */
@Entity
class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Brand of the car.
	 */
	private String brand;

	/**
	 * Registration number (license plate) of the car.
	 */
	private String registrationNumber;

	/**
	 * Model of the car.
	 */
	private String model;

	/**
	 * Identifier of the owner client.
	 */
	private Long clientId;

	public Car() {
	}

	public Car(String brand, String registrationNumber, String model, Long clientId) {
		this.brand = brand;
		this.registrationNumber = registrationNumber;
		this.model = model;
		this.clientId = clientId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
}

/**
 * Repository interface for Car entity.
 */
interface CarRepository extends JpaRepository<Car, Long> {
}
