package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.repositories.AccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccionService {

    @Autowired
    private AccionRepository repo;

    // Busca una Accion por su símbolo (case-insensitive). Devuelve null si no existe.
    public Accion buscarPorSimbolo(String simbolo) {
        if (simbolo == null) return null;
        return repo.findBySimboloIgnoreCase(simbolo).orElse(null);
    }

    public Accion crear() {
        Accion accion = new Accion();
        accion.setSimbolo("NVDIA");
        accion.setNombre("Envidia");
        return repo.save(accion);
    }

}
