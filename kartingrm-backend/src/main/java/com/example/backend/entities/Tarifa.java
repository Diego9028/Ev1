package com.example.backend.entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "tarifa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numeroVueltas;
    private int tiempoMaximoMinutos;
    private int duracionTotalMinutos;
    private int precio;
}

