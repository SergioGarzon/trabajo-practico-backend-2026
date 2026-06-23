package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.models.ItemPortfolio;
import com.utnfrc.usuario_portfolios.models.Portfolio;
import com.utnfrc.usuario_portfolios.repositories.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository repo;

    // Buscar portfolio por id (recibe String, convierte a Long)
    public Portfolio findPortfolioById(String id) {
        try {
            Long lid = Long.parseLong(id);
            return repo.findById(lid).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Portfolio findPortfolioById(Long id) {
        try {
            return repo.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Buscar un ItemPortfolio dentro de un portfolio por simbolo (case-insensitive)
    public ItemPortfolio findItemBySimbolo(Portfolio portfolio, String simbolo) {
        if (portfolio == null || simbolo == null) return null;
        for (ItemPortfolio item : portfolio.getItems()) {
            Accion a = item.getAccion();
            if (a != null && simbolo.equalsIgnoreCase(a.getSimbolo())) {
                return item;
            }
        }
        return null;
    }

    // Agregar una accion al portfolio: si no existe se agrega, si existe se suma la cantidad
    public Portfolio agregarAccion(String portfolioId, Accion accion, Long cantidad) {
        if (accion == null || cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("Accion y cantidad validas son requeridas");
        }

        Portfolio p = findPortfolioById(portfolioId);
        if (p == null) throw new IllegalArgumentException("Portfolio no encontrado");

        ItemPortfolio existente = findItemBySimbolo(p, accion.getSimbolo());
        if (existente == null) {
            p.addAccion(accion, cantidad);
        } else {
            existente.setCantidad(existente.getCantidad() + cantidad);
        }

        return repo.save(p);
    }

    // Restar una cantidad de una accion en el portfolio
    // Si la cantidad a restar es mayor a la disponible se cancela (lanza excepción)
    // Si la cantidad queda en 0 se elimina del portfolio
    public Portfolio restarAccion(String portfolioId, String simbolo, Long cantidadARestar) {
        if (simbolo == null || cantidadARestar == null || cantidadARestar <= 0) {
            throw new IllegalArgumentException("Simbolo y cantidad a restar validos son requeridos");
        }

        Portfolio p = findPortfolioById(portfolioId);
        if (p == null) throw new IllegalArgumentException("Portfolio no encontrado");

        ItemPortfolio item = findItemBySimbolo(p, simbolo);
        if (item == null) throw new IllegalArgumentException("Accion no encontrada en el portfolio");

        Long actual = item.getCantidad();
        if (cantidadARestar > actual) {
            throw new IllegalArgumentException("Cantidad a restar mayor que la disponible");
        }

        Long nueva = actual - cantidadARestar;
        if (nueva == 0) {
            // remover item del portfolio
            Iterator<ItemPortfolio> it = p.getItems().iterator();
            while (it.hasNext()) {
                ItemPortfolio ip = it.next();
                Accion a = ip.getAccion();
                if (a != null && simbolo.equalsIgnoreCase(a.getSimbolo())) {
                    it.remove();
                    break;
                }
            }
        } else {
            item.setCantidad(nueva);
        }

        return repo.save(p);
    }

}
