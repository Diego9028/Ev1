package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "politica_descuento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PoliticaDescuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private int minValor;
    private int maxValor;
    private int porcentaje;
    private int maxBeneficiados;
}