package com.example.clientservice;

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
 * Main application class for the Client Service.
 *
 * <p>
 * This microservice manages client information and registers itself
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
public class ClientServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(ClientServiceApplication.class);

	@Autowired
	private ClientRepository clientRepository;

	/**
	 * Main entry point for the Client Service application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
		logger.info("Client Service started successfully.");
	}

	/**
	 * Home endpoint for service health check or simple greeting.
	 *
	 * @return A greeting message
	 */
	@GetMapping("/")
	public String home() {
		logger.info("Home endpoint accessed.");
		return "Hello from Client Service";
	}

	/**
	 * Retrieves all clients from the database.
	 *
	 * @return List of all clients
	 */
	@GetMapping("/clients")
	public List<Client> getAllClients() {
		logger.info("Fetching all clients from the database.");
		return clientRepository.findAll();
	}
}

/**
 * JPA Entity representing a Client.
 */
@Entity
class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Full name of the client.
	 */
	private String name;

	/**
	 * Age of the client in years.
	 */
	private Float age;

	public Client() {
	}

	public Client(String name, Float age) {
		this.name = name;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getAge() {
		return age;
	}

	public void setAge(Float age) {
		this.age = age;
	}
}

/**
 * Repository interface for Client entity.
 */
interface ClientRepository extends JpaRepository<Client, Long> {
}
