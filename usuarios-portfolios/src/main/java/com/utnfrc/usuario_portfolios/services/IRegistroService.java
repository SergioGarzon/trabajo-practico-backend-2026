package com.utnfrc.usuario_portfolios.services;


import com.utnfrc.usuario_portfolios.dtos.RegistroDTO;
import com.utnfrc.usuario_portfolios.models.Usuarios;


public interface IRegistroService {
   
    public Usuarios registrarUsuarioCompleto(RegistroDTO dto);
}
