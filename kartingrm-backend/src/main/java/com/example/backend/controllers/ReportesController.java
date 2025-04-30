package com.example.backend.controllers;

import com.example.backend.entities.Reportes;
import com.example.backend.services.ReportesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportesController {

    private final ReportesService service;

    @GetMapping("/ingresos-vueltas")
    public ResponseEntity<List<Reportes>> ingresosPorVueltas(
            @RequestParam(value = "inicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(value = "fin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.generarIngresosPorVueltas(inicio, fin));
    }

    @GetMapping("/ingresos-personas")
    public ResponseEntity<List<Reportes>> ingresosPorPersonas(
            @RequestParam(value = "inicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(value = "fin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(service.generarIngresosPorPersonas(inicio, fin));
    }
}
