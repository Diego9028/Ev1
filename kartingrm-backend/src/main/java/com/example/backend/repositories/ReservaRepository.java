package com.example.backend.repositories;

import com.example.backend.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    int countByClientePrincipalIdAndFechaHoraBetween(
            Long clienteId,
            LocalDateTime inicio,
            LocalDateTime fin);

}
