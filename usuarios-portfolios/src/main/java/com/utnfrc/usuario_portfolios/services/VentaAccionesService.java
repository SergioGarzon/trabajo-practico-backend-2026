package com.utnfrc.usuario_portfolios.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utnfrc.usuario_portfolios.dtos.ResolucionVentaDTO;
import com.utnfrc.usuario_portfolios.dtos.SolicitudVentaDTO;
import com.utnfrc.usuario_portfolios.excepciones.ResourceNotFoundException;
import com.utnfrc.usuario_portfolios.excepciones.TransaccionInversionException;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;
import com.utnfrc.usuario_portfolios.models.ItemPortfolio;
import com.utnfrc.usuario_portfolios.models.OrdenVenta;
import com.utnfrc.usuario_portfolios.models.Portfolio;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.BilleteraVirtualRepository;
import com.utnfrc.usuario_portfolios.repositories.OrdenVentaRepository;
import com.utnfrc.usuario_portfolios.repositories.PortfolioRepository;

import jakarta.transaction.Transactional;

@Service
public class VentaAccionesService implements IVentaAccionesService {

    private final UsuariosServices usuariosServices;
    private final OrdenVentaRepository ordenVentaRepository;
    private final BilleteraVirtualRepository billeteraRepository;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    public VentaAccionesService(UsuariosServices usuariosServices, OrdenVentaRepository ordenVentaRepository, BilleteraVirtualRepository billeteraRepository, PortfolioRepository portfolioRepository) {
        this.usuariosServices = usuariosServices;
        this.ordenVentaRepository = ordenVentaRepository;
        this.billeteraRepository = billeteraRepository;
        this.portfolioRepository = portfolioRepository;
    }


    @Transactional
    public OrdenVenta iniciarVenta(String userId, SolicitudVentaDTO dto) {
        Usuarios usuario = usuariosServices.getById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Portfolio portfolio = usuario.getPortfolio();


        ItemPortfolio item = portfolio.getItems().stream()
                .filter(i -> i.getAccion().getSimbolo().equalsIgnoreCase(dto.getSimboloAccion()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No posees acciones de " + dto.getSimboloAccion()));

        if (item.getCantidadLibre() < dto.getCantidadAVender()) {
            throw new TransaccionInversionException("No tienes cantidad libre suficiente para vender.");
        }


        item.setCantidadLibre(item.getCantidadLibre() - dto.getCantidadAVender());
        item.setCantidadBloqueada(item.getCantidadBloqueada() + dto.getCantidadAVender());


        OrdenVenta orden = new OrdenVenta(dto.getIdOrdenVenta());
        orden.setItemPortfolio(item);
        orden.setCantidadInicial(dto.getCantidadAVender());
        orden.setCantidadRestante(dto.getCantidadAVender());

        ordenVentaRepository.save(orden);
        portfolioRepository.save(portfolio);

        return orden;
    }


    @Transactional
    public void procesarVenta(ResolucionVentaDTO dto) {

        // verifica con el id que le pasan si existe la orden
        OrdenVenta orden = ordenVentaRepository.findById(dto.getIdOrdenVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Orden de venta no encontrada"));

        // verifica que no este ya relizada
        if (orden.getEstado().equals("COMPLETADA") || orden.getEstado().equals("CANCELADA")) {
            throw new TransaccionInversionException("Esta orden ya está cerrada o cancelada.");
        }

        //Verifica que no se quiera vender mas de lo que tiene a la venta
        if (orden.getCantidadRestante() < dto.getCantidadVendida()){
            throw new TransaccionInversionException("Estas intentado vender mas de lo que tiene");
        }

        // Se busca que el item porfolio que tiene la infomacion sobre las acciones a la venta
        ItemPortfolio item = orden.getItemPortfolio();
        BilleteraVirtual bv = item.getPortfolio().getUsuario().getBilleteraVirtual();

        // Actualizamos los contadores, desbloqueamos las acciones
        item.setCantidadBloqueada(item.getCantidadBloqueada() - dto.getCantidadVendida());
        orden.setCantidadRestante(orden.getCantidadRestante() - dto.getCantidadVendida());
        // Sumamos el dinero obtenido
        bv.setDineroLibre(bv.getDineroLibre() + dto.getDineroObtenido());

        // validamos si nos quedan acciones por vender o no
        if (orden.getCantidadRestante() == 0) {
            orden.setEstado("COMPLETADA");
        } else {
            orden.setEstado("PARCIAL");
        }

        // si nos quedamos si acciones se debe quitar del portfolio
        if (item.getCantidadLibre() == 0 && item.getCantidadBloqueada() == 0) {
            item.getPortfolio().getItems().remove(item);
        }

        billeteraRepository.save(bv);
        ordenVentaRepository.save(orden);

    }


    @Transactional
    public void cancelarOrdenVenta(String idOrdenVenta) {
        OrdenVenta orden = ordenVentaRepository.findById(Long.valueOf(idOrdenVenta))
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (orden.getEstado().equals("COMPLETADA") || orden.getEstado().equals("CANCELADA")) {
            throw new TransaccionInversionException("No se puede cancelar una orden cerrada.");
        }

        ItemPortfolio item = orden.getItemPortfolio();

        item.setCantidadLibre(item.getCantidadLibre() + orden.getCantidadRestante());
        item.setCantidadBloqueada(item.getCantidadBloqueada() - orden.getCantidadRestante());

        orden.setCantidadRestante(0L);
        orden.setEstado("CANCELADA");

        ordenVentaRepository.save(orden);
    }
}