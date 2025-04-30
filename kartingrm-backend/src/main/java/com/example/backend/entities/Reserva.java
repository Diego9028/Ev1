package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Tarifa tarifa;

    @ManyToOne(optional = false)
    private Cliente clientePrincipal;

    private LocalDateTime fechaHora;

    private int cantidadPersonas;

    @ManyToMany
    @JoinTable(
            name = "reserva_clientes",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )
    private List<Cliente> clientes;

    private int costoReserva;
    private int descuentoGrupo;
    private int descuentoFrecuente;
    private int descuentoEspecial;
    private int montoFinal;
    private int iva;
    private int totalConIva;

    private String estado = "ACTIVA";


    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comprobante> comprobantes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "reserva_karts", joinColumns = @JoinColumn(name = "reserva_id"))
    @Column(name = "kart_id")
    private List<Long> kartsUsados = new ArrayList<>();

}
