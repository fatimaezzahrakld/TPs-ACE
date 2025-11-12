package com.example.Spring.Data.REST;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compte {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double solde;
    
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Enumerated(EnumType.STRING)
    private TypeCompte type;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
