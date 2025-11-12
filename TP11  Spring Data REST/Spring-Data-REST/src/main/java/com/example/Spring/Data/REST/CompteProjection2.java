package com.example.Spring.Data.REST;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "mobile", types = Compte.class)
public interface CompteProjection2 {
    double getSolde();
    TypeCompte getType();
}
