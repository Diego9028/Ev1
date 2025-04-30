package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reportes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** INGRESOS_VUELTAS | INGRESOS_PERSONAS */
    private String tipo;

    /** "10 vueltas", "3-5 personas", etc. */
    private String criterio;

    private Long totalIngresos;

    private LocalDateTime fechaGeneracion;
}