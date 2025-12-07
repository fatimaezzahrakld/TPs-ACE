package com.example.Client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Client.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> { }
