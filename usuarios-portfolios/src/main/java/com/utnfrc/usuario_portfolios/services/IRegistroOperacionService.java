package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.models.RegistroOperacion;
import com.utnfrc.usuario_portfolios.models.TipoOperacion;
import com.utnfrc.usuario_portfolios.models.Usuarios;

import java.util.List;

public interface IRegistroOperacionService {
    void registrar(Usuarios usuario, TipoOperacion tipo, String descripcion, Double monto);
    List<RegistroOperacion> obtenerHistorialPorUsuario(Long usuarioId);
}
