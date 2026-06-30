package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.models.RegistroOperacion;
import com.utnfrc.usuario_portfolios.models.TipoOperacion;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.RegistroOperacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroOperacionService implements IRegistroOperacionService {

    @Autowired
    private RegistroOperacionRepository repository;

    // Método para crear un nuevo registro
    public void registrar(Usuarios usuario, TipoOperacion tipo, String descripcion, Double monto) {
        RegistroOperacion registro = new RegistroOperacion();
        registro.setUsuario(usuario);
        registro.setTipoOperacion(tipo);
        registro.setDescripcion(descripcion);
        registro.setMonto(monto);

        repository.save(registro);
    }

    // Método para obtener el historial
    public List<RegistroOperacion> obtenerHistorialPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
    }
}