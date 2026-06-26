package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.repositories.AccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.utnfrc.usuario_portfolios.dtos.AccionExternaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccionService implements IAccionService {

    @Autowired
    private AccionRepository repo;
    @Autowired
    private RestTemplate restTemplate;

    public Accion buscarPorSimbolo(String simbolo) {
        if (simbolo == null) return null;
        return repo.findBySimbolo(simbolo).orElse(null);
    }

    public Accion crear() {
        Accion accion = new Accion();
        accion.setSimbolo("NVDIA");
        accion.setNombre("Envidia");
        return repo.save(accion);
    }

    public List<Accion> buscarTodos() {return repo.findAll();}

    @Transactional
    public List<Accion> sincronizarAcciones() {

        String urlServicioExterno = "http://localhost:8084/servidormock/api/v1/stocks/all-stocks";

        ResponseEntity<AccionExternaDTO[]> response = restTemplate.getForEntity(
                urlServicioExterno,
                AccionExternaDTO[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

            List<Accion> nuevasAccionesAGuardar = new ArrayList<>();
            AccionExternaDTO[] accionesRecibidas = response.getBody();

            for (AccionExternaDTO dto : accionesRecibidas) {

                if (repo.findBySimbolo(dto.getSimbolo()).isEmpty()) {

                    Accion nuevaAccion = new Accion();
                    nuevaAccion.setNombre(dto.getNombre());
                    nuevaAccion.setSimbolo(dto.getSimbolo());

                    nuevasAccionesAGuardar.add(nuevaAccion);
                }
            }

            if (!nuevasAccionesAGuardar.isEmpty()) {
                return repo.saveAll(nuevasAccionesAGuardar);
            } else {
                return new ArrayList<>();
            }

        } else {
            throw new RuntimeException("Error de comunicación: No se pudo obtener la lista de acciones del servicio.");
        }
    }
}

