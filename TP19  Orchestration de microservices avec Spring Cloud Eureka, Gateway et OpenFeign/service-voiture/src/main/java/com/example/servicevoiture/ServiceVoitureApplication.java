package com.example.servicevoiture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.example.servicevoiture.repositories.VoitureRepository;
import com.example.servicevoiture.entities.Voiture;
import com.example.servicevoiture.feign.ClientService;
import com.example.servicevoiture.models.Client;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceVoitureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceVoitureApplication.class, args);
    }

    @Bean
    CommandLineRunner initialiserBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
        return args -> {
            Client c1 = clientService.clientById(2L);
            Client c2 = clientService.clientById(1L);
            System.out.println("**************************");
            System.out.println("Id est :" + c2.getId());
            System.out.println("Nom est :" + c2.getNom());
            System.out.println("**************************");
            System.out.println("**************************");
            System.out.println("Id est :" + c1.getId());
            System.out.println("Nom est :" + c1.getNom());
            System.out.println("Age est :" + c1.getAge());
            System.out.println("**************************");
            voitureRepository.save(new Voiture(1L, "Toyota", "A 25 333", "Corolla", 1L, c2));
            voitureRepository.save(new Voiture(2L, "Renault", "B 6 3456", "Megane", 1L, c2));
            voitureRepository.save(new Voiture(3L, "Peugeot", "A 55 4444", "301", 2L, c1));
        };
    }

}
