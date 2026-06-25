package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.excepciones.PortfolioException;
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
        } catch (PortfolioException e) {
            return null;
        }
    }

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

    public Portfolio agregarAccion(Long portfolioId, Accion accion, Long cantidad) {
        if (accion == null || cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("Accion y cantidad validas son requeridas");
        }

        Portfolio p = findPortfolioById(portfolioId);
        if (p == null) throw new IllegalArgumentException("Portfolio no encontrado");

        ItemPortfolio existente = findItemBySimbolo(p, accion.getSimbolo());
        if (existente == null) {
            p.addAccion(accion, cantidad);
        } else {
            existente.setCantidadLibre(existente.getCantidadLibre() + cantidad);
        }

        return repo.save(p);
    }


    public Portfolio restarAccion(String portfolioId, String simbolo, Long cantidadARestar) {
        if (simbolo == null || cantidadARestar == null || cantidadARestar <= 0) {
            throw new IllegalArgumentException("Simbolo y cantidad a restar validos son requeridos");
        }

        Portfolio p = findPortfolioById(portfolioId);
        if (p == null) throw new IllegalArgumentException("Portfolio no encontrado");

        ItemPortfolio item = findItemBySimbolo(p, simbolo);
        if (item == null) throw new IllegalArgumentException("Accion no encontrada en el portfolio");

        Long actual = item.getCantidadLibre();
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
            item.setCantidadLibre(nueva);
        }

        return repo.save(p);
    }

}
