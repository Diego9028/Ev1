package com.example.backend.repositories;


import com.example.backend.entities.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    Optional<Tarifa> findByNumeroVueltasAndTiempoMaximoMinutos(int numeroVueltas, int tiempoMaximoMinutos);
}

