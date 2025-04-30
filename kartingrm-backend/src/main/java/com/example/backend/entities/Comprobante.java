package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comprobante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    @JsonBackReference
    private Reserva reserva;


    // Datos de desglose individual
    private int tarifaBase;
    private int descuentoGrupo;
    private int descuentoFrecuente;
    private int descuentoEspecial;
    private int subtotal;
    private int iva;
    private int total;
}
