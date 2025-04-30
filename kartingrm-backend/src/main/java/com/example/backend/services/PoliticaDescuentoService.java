package com.example.backend.services;

import com.example.backend.entities.PoliticaDescuento;
import com.example.backend.repositories.PoliticaDescuentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PoliticaDescuentoService {

    @Autowired
    private PoliticaDescuentoRepository politicaDescuentoRepository;

    public List<PoliticaDescuento> obtenerTodas() {
        return politicaDescuentoRepository.findAll();
    }

    public Optional<PoliticaDescuento> buscarPorId(Long id) {
        return politicaDescuentoRepository.findById(id);
    }

    public List<PoliticaDescuento> buscarPorTipo(String tipo) {
        return politicaDescuentoRepository.findByTipo(tipo);
    }

    public PoliticaDescuento guardar(PoliticaDescuento politica) {
        return politicaDescuentoRepository.save(politica);
    }

    public void eliminarPorId(Long id) {
        politicaDescuentoRepository.deleteById(id);
    }

    public PoliticaDescuento actualizar(Long id, PoliticaDescuento nuevaPolitica) {
        return politicaDescuentoRepository.findById(id)
                .map(politicaExistente -> {
                    politicaExistente.setTipo(nuevaPolitica.getTipo());
                    politicaExistente.setMinValor(nuevaPolitica.getMinValor());
                    politicaExistente.setMaxValor(nuevaPolitica.getMaxValor());
                    politicaExistente.setPorcentaje(nuevaPolitica.getPorcentaje());
                    politicaExistente.setMaxBeneficiados(nuevaPolitica.getMaxBeneficiados());
                    return politicaDescuentoRepository.save(politicaExistente);
                })
                .orElseThrow(() -> new RuntimeException("Política no encontrada con ID: " + id));
    }

}
