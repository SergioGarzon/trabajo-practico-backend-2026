package com.utnfrc.usuario_portfolios.services;


import com.utnfrc.usuario_portfolios.dtos.ResolucionVentaDTO;
import com.utnfrc.usuario_portfolios.dtos.SolicitudVentaDTO;
import com.utnfrc.usuario_portfolios.models.OrdenVenta;


public interface IVentaAccionesService {

    
    public OrdenVenta iniciarVenta(String userId, SolicitudVentaDTO dto);

    public void procesarVenta(ResolucionVentaDTO dto);

    public void cancelarOrdenVenta(Long idOrdenVenta);
}
