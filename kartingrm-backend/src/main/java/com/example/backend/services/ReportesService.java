package com.example.backend.services;

import com.example.backend.entities.Reportes;
import com.example.backend.entities.Reserva;
import com.example.backend.repositories.ReportesRepository;
import com.example.backend.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportesService {

    private final ReservaRepository reservaRepo;
    private final ReportesRepository reportesRepo;


    public List<Reportes> generarIngresosPorVueltas(LocalDateTime inicio, LocalDateTime fin) {
        List<Reserva> reservas = recuperarReservas(inicio, fin);
        Map<Integer, Long> acumulados = new HashMap<>();

        reservas.forEach(r -> {
            int vueltas = r.getTarifa().getNumeroVueltas();
            acumulados.merge(vueltas, (long) r.getTotalConIva(), Long::sum);
        });

        List<Reportes> resultado = acumulados.entrySet().stream()
                .map(e -> Reportes.builder()
                        .tipo("INGRESOS_VUELTAS")
                        .criterio(e.getKey() + " vueltas")
                        .totalIngresos(e.getValue())
                        .fechaGeneracion(LocalDateTime.now())
                        .build())
                .toList();

        return reportesRepo.saveAll(resultado);
    }

    public List<Reportes> generarIngresosPorPersonas(LocalDateTime inicio, LocalDateTime fin) {
        List<Reserva> reservas = recuperarReservas(inicio, fin);
        Map<String, Long> acumulados = new HashMap<>();

        reservas.forEach(r -> {
            String rango = rangoPersonas(r.getCantidadPersonas());
            acumulados.merge(rango, (long) r.getTotalConIva(), Long::sum);
        });

        List<Reportes> resultado = acumulados.entrySet().stream()
                .map(e -> Reportes.builder()
                        .tipo("INGRESOS_PERSONAS")
                        .criterio(e.getKey())
                        .totalIngresos(e.getValue())
                        .fechaGeneracion(LocalDateTime.now())
                        .build())
                .toList();

        return reportesRepo.saveAll(resultado);
    }

    private List<Reserva> recuperarReservas(LocalDateTime inicio, LocalDateTime fin) {
        if (inicio == null || fin == null) {
            return reservaRepo.findAll();
        }
        return reservaRepo.findByFechaHoraBetween(inicio, fin);
    }

    private String rangoPersonas(int n) {
        if (n <= 2) return "1-2 personas";
        if (n <= 5) return "3-5 personas";
        if (n <= 10) return "6-10 personas";
        return "11-15 personas";
    }
}