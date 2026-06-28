package com.utnfrc.usuario_portfolios.services;

import java.util.List;
import java.util.Optional;

import com.utnfrc.usuario_portfolios.dtos.SolicitudDineroDTO;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;

public interface IBilleteraVirtualService {	

    BilleteraVirtual createBV(String usuarioID);

    List<BilleteraVirtual> findAllBV();
    
    Optional<BilleteraVirtual> findBVById(Long id);

    void deleteBV(Long dni);

    BilleteraVirtual retirarDinero(String idUsuario, Long cantidad);

    BilleteraVirtual ingresarDinero(String idUsuario, Long cantidad);

    String solicitarYBloquearDinero(String keycloakUserId, Long monto, String idOrdenCompra);

    BilleteraVirtual procesarRespuestaExterna(String userID, SolicitudDineroDTO dto);
}
