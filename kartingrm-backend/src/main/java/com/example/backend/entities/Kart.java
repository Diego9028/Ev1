package com.example.backend.entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "kart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Kart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private String estado;

    private String modelo;
}