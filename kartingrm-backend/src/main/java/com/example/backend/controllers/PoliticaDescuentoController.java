package com.example.backend.controllers;

import com.example.backend.entities.PoliticaDescuento;
import com.example.backend.services.PoliticaDescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/descuentos")
@CrossOrigin(origins = "*")
public class PoliticaDescuentoController {

    @Autowired
    private PoliticaDescuentoService descuentoService;

    @GetMapping
    public ResponseEntity<List<PoliticaDescuento>> obtenerTodas() {
        return ResponseEntity.ok(descuentoService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PoliticaDescuento> obtenerPorId(@PathVariable Long id) {
        Optional<PoliticaDescuento> politica = descuentoService.buscarPorId(id);
        return politica.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<PoliticaDescuento>> obtenerPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(descuentoService.buscarPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<PoliticaDescuento> crear(@RequestBody PoliticaDescuento politica) {
        return ResponseEntity.ok(descuentoService.guardar(politica));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PoliticaDescuento> actualizar(@PathVariable Long id, @RequestBody PoliticaDescuento politicaActualizada) {
        try {
            PoliticaDescuento actualizada = descuentoService.actualizar(id, politicaActualizada);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (descuentoService.buscarPorId(id).isPresent()) {
            descuentoService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
