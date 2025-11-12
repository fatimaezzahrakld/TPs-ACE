package com.example.Spring.Data.REST;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "clientDetails", types = Client.class)
public interface ClientProjection {
    public String getNom();
    public String getEmail();
}
