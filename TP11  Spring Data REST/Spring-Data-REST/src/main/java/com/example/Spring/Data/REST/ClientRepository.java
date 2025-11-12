package com.example.Spring.Data.REST;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "clients", collectionResourceRel = "clients", itemResourceRel = "client")
public interface ClientRepository extends JpaRepository<Client, Long> {
}
