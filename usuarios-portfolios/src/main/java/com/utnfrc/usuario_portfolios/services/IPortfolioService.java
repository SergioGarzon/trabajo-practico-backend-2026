package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.models.Accion;
import com.utnfrc.usuario_portfolios.models.ItemPortfolio;
import com.utnfrc.usuario_portfolios.models.Portfolio;

public interface IPortfolioService {


	Portfolio findPortfolioById(String id);

	Portfolio findPortfolioById(Long id);

	ItemPortfolio findItemBySimbolo(Portfolio portfolio, String simbolo);

	Portfolio agregarAccion(Long portfolioId, Accion accion, Long cantidad);

	Portfolio restarAccion(String portfolioId, String simbolo, Long cantidadARestar);
}
