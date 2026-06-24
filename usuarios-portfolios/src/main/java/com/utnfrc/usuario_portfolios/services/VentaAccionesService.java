package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.dtos.ResolucionVentaDTO;
import com.utnfrc.usuario_portfolios.dtos.SolicitudVentaDTO;
import com.utnfrc.usuario_portfolios.excepciones.ResourceNotFoundException;
import com.utnfrc.usuario_portfolios.excepciones.TransaccionInversionException;
import com.utnfrc.usuario_portfolios.models.*;
import com.utnfrc.usuario_portfolios.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class VentaAccionesService {

    private final UsuariosServices usuariosServices;
    private final OrdenVentaRepository ordenVentaRepository;
    private final BilleteraVirtualRepository billeteraRepository;
    private final PortfolioRepository portfolioRepository;

    public VentaAccionesService(UsuariosServices usuariosServices, OrdenVentaRepository ordenVentaRepository, BilleteraVirtualRepository billeteraRepository, PortfolioRepository portfolioRepository) {
        this.usuariosServices = usuariosServices;
        this.ordenVentaRepository = ordenVentaRepository;
        this.billeteraRepository = billeteraRepository;
        this.portfolioRepository = portfolioRepository;
    }


    @Transactional
    public String iniciarVenta(String userId, SolicitudVentaDTO dto) {
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


        OrdenVenta orden = new OrdenVenta();
        orden.setItemPortfolio(item);
        orden.setCantidadInicial(dto.getCantidadAVender());
        orden.setCantidadRestante(dto.getCantidadAVender());

        ordenVentaRepository.save(orden);
        portfolioRepository.save(portfolio);

        return orden.getId();
    }


    @Transactional
    public void procesarVenta(ResolucionVentaDTO dto) {
        OrdenVenta orden = ordenVentaRepository.findById(dto.getIdOrdenVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Orden de venta no encontrada"));

        if (orden.getEstado().equals("COMPLETADA") || orden.getEstado().equals("CANCELADA")) {
            throw new TransaccionInversionException("Esta orden ya está cerrada o cancelada.");
        }

        ItemPortfolio item = orden.getItemPortfolio();
        BilleteraVirtual bv = item.getPortfolio().getUsuario().getBilleteraVirtual();

        item.setCantidadBloqueada(item.getCantidadBloqueada() - dto.getCantidadVendida());
        orden.setCantidadRestante(orden.getCantidadRestante() - dto.getCantidadVendida());

        bv.setDineroLibre(bv.getDineroLibre() + dto.getDineroObtenido());

        if (orden.getCantidadRestante() == 0) {
            orden.setEstado("COMPLETADA");
        } else {
            orden.setEstado("PARCIAL");
        }

        if (item.getCantidadLibre() == 0 && item.getCantidadBloqueada() == 0) {
            item.getPortfolio().getItems().remove(item);
        }

        billeteraRepository.save(bv);
        ordenVentaRepository.save(orden);

    }


    @Transactional
    public void cancelarOrdenVenta(String idOrdenVenta) {
        OrdenVenta orden = ordenVentaRepository.findById(idOrdenVenta)
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