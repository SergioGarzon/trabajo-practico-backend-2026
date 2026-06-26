package com.utnfrc.usuario_portfolios.services;

import java.util.List;

import com.utnfrc.usuario_portfolios.models.Accion;

public interface IAccionService {
	
    Accion buscarPorSimbolo(String simbolo);

    Accion crear();

    List<Accion> buscarTodos();


    List<Accion> sincronizarAcciones();
  }
