package com.example.backend.repositories;

import com.example.backend.entities.Kart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KartRepository extends JpaRepository<Kart, Long> {

    List<Kart> findByEstado(String estado);


}

