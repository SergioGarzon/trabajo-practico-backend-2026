package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.repositories.AccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.utnfrc.usuario_portfolios.dtos.AccionExternaDTO;
import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.repositories.AccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccionService {

    @Autowired
    private AccionRepository repo;
    @Autowired
    private RestTemplate restTemplate;

    // Busca una Accion por su símbolo (case-insensitive). Devuelve null si no existe.
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

    @Transactional
    public List<Accion> sincronizarAcciones() {

        // 1. Definir la URL del otro microservicio (o apuntar al API Gateway)
        // Reemplazá con el puerto y la ruta real del servicio que tiene las acciones
        String urlServicioExterno = "http://localhost:8082/api/acciones-disponibles";

        // 2. Hacer la petición GET.
        // Como devuelve una lista/array de objetos, lo mapeamos a un Array del DTO.
        ResponseEntity<AccionExternaDTO[]> response = restTemplate.getForEntity(
                urlServicioExterno,
                AccionExternaDTO[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

            List<Accion> nuevasAccionesAGuardar = new ArrayList<>();
            AccionExternaDTO[] accionesRecibidas = response.getBody();

            // 3. Iterar sobre la respuesta y mapear a Entidad
            for (AccionExternaDTO dto : accionesRecibidas) {

                // CONTROL DEFENSIVO: Verificamos que el símbolo no exista ya en la BD
                // (Asumiendo que tenés un Optional<Accion> findBySimbolo(String simbolo) en tu Repositorio)
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
                return new ArrayList<>(); // No hubo acciones nuevas para guardar
            }

        } else {
            throw new RuntimeException("Error de comunicación: No se pudo obtener la lista de acciones del servicio.");
        }
    }
}


