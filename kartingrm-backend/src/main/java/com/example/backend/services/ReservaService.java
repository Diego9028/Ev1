package com.example.backend.services;

import com.example.backend.entities.*;
import com.example.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired private ReservaRepository reservaRepository;
    @Autowired private PoliticaDescuentoRepository politicaDescuentoRepository;
    @Autowired private TarifaRepository tarifaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private KartRepository kartRepository;

    private static final double IVA = 0.19;

    public Reserva guardar(Reserva reserva) {
        Tarifa tarifa = tarifaRepository.findById(reserva.getTarifa().getId())
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));
        reserva.setTarifa(tarifa);

        LocalDateTime inicio = reserva.getFechaHora();
        LocalDateTime fin    = inicio.plusMinutes(tarifa.getDuracionTotalMinutos());

        List<Reserva> reservasSolapadas = reservaRepository.findByFechaHoraBetween(
                inicio.minusMinutes(tarifa.getDuracionTotalMinutos()), fin
        );
        int personasReservadas = reservasSolapadas.stream().mapToInt(Reserva::getCantidadPersonas).sum();

        int kartsDisponibles = cantidadKartsDisponibles();
        int libres = kartsDisponibles - personasReservadas;
        if (reserva.getCantidadPersonas() > libres) {
            throw new IllegalStateException("Sólo hay " + libres + " karts disponibles en este horario.");
        }


        List<Kart> asignados = kartRepository.findByEstado("DISPONIBLE").stream()
                .limit(reserva.getCantidadPersonas()).toList();
        asignados.forEach(k -> reserva.getKartsUsados().add(k.getId()));

        LocalDate fechaReserva = inicio.toLocalDate();
        boolean esFinde   = inicio.getDayOfWeek() == DayOfWeek.SATURDAY || inicio.getDayOfWeek() == DayOfWeek.SUNDAY;
        boolean esFeriado = esFeriado(fechaReserva);

        int base = tarifa.getPrecio();
        int descuentoFinde   = esFinde   ? 10 : 0;
        int descuentoFeriado = esFeriado ? 20 : 0;

        List<Comprobante> comprobantes = new java.util.ArrayList<>();
        int totalReserva = 0;

        for (Cliente stub : reserva.getClientes()) {
            Cliente cliente = clienteRepository.findById(stub.getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));


            YearMonth ym = YearMonth.from(inicio);
            LocalDateTime mesInicio = ym.atDay(1).atStartOfDay();
            LocalDateTime mesFin    = ym.plusMonths(1).atDay(1).atStartOfDay();
            int visitasMes = reservaRepository.countByClientePrincipalIdAndFechaHoraBetween(
                    cliente.getId(), mesInicio, mesFin);

            int descuentoGrupo  = calcularDescuento("GRUPO", reserva.getCantidadPersonas());
            int descuentoFreq   = calcularDescuentoFrecuencia(visitasMes + 1);
            int descuentoCumple = esCumpleHoy(cliente) ? 50 : 0;
            int descuentoEspecial = descuentoFinde + descuentoFeriado + descuentoCumple;

            int totalDescuento = descuentoGrupo + descuentoFreq + descuentoEspecial;
            int neto     = base - (base * totalDescuento / 100);
            int iva      = (int) (neto * IVA);
            int total    = neto + iva;

            Comprobante comp = new Comprobante();
            comp.setCliente(cliente);
            comp.setReserva(reserva);
            comp.setTarifaBase(base);
            comp.setDescuentoGrupo(descuentoGrupo);
            comp.setDescuentoFrecuente(descuentoFreq);
            comp.setDescuentoEspecial(descuentoEspecial);
            comp.setSubtotal(neto);
            comp.setIva(iva);
            comp.setTotal(total);

            comprobantes.add(comp);
            totalReserva += total;
        }

        reserva.setCostoReserva(base);
        reserva.setComprobantes(comprobantes);
        reserva.setTotalConIva(totalReserva);

        return reservaRepository.save(reserva);
    }

    public int cantidadKartsDisponibles() {
        return kartRepository.findByEstado("DISPONIBLE").size();
    }

    private int calcularDescuento(String tipo, int valor) {
        return politicaDescuentoRepository.findByTipo(tipo).stream()
                .filter(p -> valor >= p.getMinValor() && valor <= p.getMaxValor())
                .mapToInt(PoliticaDescuento::getPorcentaje)
                .findFirst().orElse(0);
    }

    private int calcularDescuentoFrecuencia(int visitasMes) {
        if (visitasMes >= 7) return 30;
        if (visitasMes >= 5) return 20;
        if (visitasMes >= 2) return 10;
        return 0;
    }

    private boolean esCumpleHoy(Cliente cliente) {
        if (cliente == null || cliente.getFechaNacimiento() == null) return false;
        LocalDate hoy = LocalDate.now();
        LocalDate n   = cliente.getFechaNacimiento();
        return hoy.getDayOfMonth() == n.getDayOfMonth() && hoy.getMonth() == n.getMonth();
    }

    private boolean esFeriado(LocalDate fecha) {
        return List.of(
                LocalDate.of(fecha.getYear(), 1, 1),
                LocalDate.of(fecha.getYear(), 9, 18),
                LocalDate.of(fecha.getYear(), 9, 19),
                LocalDate.of(fecha.getYear(), 12, 25)
        ).contains(fecha);
    }


    public List<Reserva> obtenerTodas() { return reservaRepository.findAll(); }
    public Optional<Reserva> obtenerPorId(Long id) { return reservaRepository.findById(id); }

    public List<Reserva> obtenerSemana(LocalDate primerDia) {
        LocalDateTime i = primerDia.atStartOfDay();
        LocalDateTime f = i.plusDays(7);
        return reservaRepository.findByFechaHoraBetween(i, f);
    }

    public Reserva actualizarEstado(Long id, String nuevoEstado) {
        Reserva r = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        r.setEstado(nuevoEstado);
        return reservaRepository.save(r);
    }
}
