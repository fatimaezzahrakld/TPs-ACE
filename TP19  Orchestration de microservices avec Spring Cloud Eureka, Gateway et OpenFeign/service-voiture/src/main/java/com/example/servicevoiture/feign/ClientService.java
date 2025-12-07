package com.example.servicevoiture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.servicevoiture.models.Client;

@FeignClient(name = "SERVICE-CLIENT")
public interface ClientService {

    @GetMapping(path = "/clients/{id}")
    Client clientById(@PathVariable Long id);

}
