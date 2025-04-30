package com.example.backend.repositories;

import com.example.backend.entities.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {

    List<Comprobante> findByReservaId(Long reservaId);
}
