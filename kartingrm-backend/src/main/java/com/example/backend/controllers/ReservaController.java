package com.example.backend.controllers;
import com.example.backend.entities.Reserva;
import com.example.backend.repositories.ReservaRepository;
import com.example.backend.services.ReservaService;
import com.example.backend.services.ComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ComprobanteService comprobanteService;

    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@RequestBody Reserva reserva) {
        Reserva nueva = reservaService.guardar(reserva);
            try {
                comprobanteService.enviarComprobantePorEmail(nueva);
            } catch (Exception e) {e.printStackTrace();}
        return ResponseEntity.ok(nueva);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        Optional<Reserva> reserva = reservaService.obtenerPorId(id);
        return reserva.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/semana")
    public List<Reserva> listarPorSemana(
            @RequestParam("primerDia")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate primerDia) {

        return reservaService.obtenerSemana(primerDia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        reservaRepository.delete(reservaOpt.get());
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reserva> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            Reserva actualizada = reservaService.actualizarEstado(id, estado);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
