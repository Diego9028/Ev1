package com.example.backend.repositories;

import com.example.backend.entities.PoliticaDescuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliticaDescuentoRepository extends JpaRepository<PoliticaDescuento, Long> {
    List<PoliticaDescuento> findByTipo(String tipo);
}
