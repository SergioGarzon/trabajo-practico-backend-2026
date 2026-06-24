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

    /**
     * FASE 1: El usuario solicita vender. Bloqueamos las acciones y devolvemos el UUID.
     */
    @Transactional
    public String iniciarVenta(String userId, SolicitudVentaDTO dto) {
        Usuarios usuario = usuariosServices.getById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Portfolio portfolio = usuario.getPortfolio();

        // Buscamos si tiene esa acción en su portfolio
        ItemPortfolio item = portfolio.getItems().stream()
                .filter(i -> i.getAccion().getSimbolo().equalsIgnoreCase(dto.getSimboloAccion()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No posees acciones de " + dto.getSimboloAccion()));

        if (item.getCantidadLibre() < dto.getCantidadAVender()) {
            throw new TransaccionInversionException("No tienes cantidad libre suficiente para vender.");
        }

        // 1. Bloqueamos las acciones
        item.setCantidadLibre(item.getCantidadLibre() - dto.getCantidadAVender());
        item.setCantidadBloqueada(item.getCantidadBloqueada() + dto.getCantidadAVender());

        // 2. Creamos el registro de trazabilidad
        OrdenVenta orden = new OrdenVenta();
        orden.setItemPortfolio(item);
        orden.setCantidadInicial(dto.getCantidadAVender());
        orden.setCantidadRestante(dto.getCantidadAVender());

        ordenVentaRepository.save(orden);
        portfolioRepository.save(portfolio); // Guarda en cascada el item actualizado

        return orden.getId();
    }

    /**
     * FASE 2: El motor externo avisa que vendió una parte (o todo).
     */
    @Transactional
    public void procesarVenta(ResolucionVentaDTO dto) {
        OrdenVenta orden = ordenVentaRepository.findById(dto.getIdOrdenVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Orden de venta no encontrada"));

        if (orden.getEstado().equals("COMPLETADA") || orden.getEstado().equals("CANCELADA")) {
            throw new TransaccionInversionException("Esta orden ya está cerrada o cancelada.");
        }

        ItemPortfolio item = orden.getItemPortfolio();
        BilleteraVirtual bv = item.getPortfolio().getUsuario().getBilleteraVirtual();

        // 1. Descontamos las acciones bloqueadas de forma definitiva
        item.setCantidadBloqueada(item.getCantidadBloqueada() - dto.getCantidadVendida());
        orden.setCantidadRestante(orden.getCantidadRestante() - dto.getCantidadVendida());

        // 2. Ingresamos el dinero a la billetera
        bv.setDineroLibre(bv.getDineroLibre() + dto.getDineroObtenido());

        // 3. Actualizamos estado de la orden
        if (orden.getCantidadRestante() == 0) {
            orden.setEstado("COMPLETADA");
        } else {
            orden.setEstado("PARCIAL");
        }

        // 4. LIMPIEZA: Si el usuario ya no tiene acciones libres ni bloqueadas de este símbolo, lo quitamos del portfolio
        if (item.getCantidadLibre() == 0 && item.getCantidadBloqueada() == 0) {
            item.getPortfolio().getItems().remove(item);
        }

        billeteraRepository.save(bv);
        ordenVentaRepository.save(orden);
        // Al guardar el portfolio (o si JPA detecta el cambio en la lista), se elimina el ItemPortfolio si orphanRemoval=true
    }

    /**
     * EXTRA: El usuario se arrepiente y cancela la venta (de lo que quede sin vender).
     */
    @Transactional
    public void cancelarOrdenVenta(String idOrdenVenta) {
        OrdenVenta orden = ordenVentaRepository.findById(idOrdenVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (orden.getEstado().equals("COMPLETADA") || orden.getEstado().equals("CANCELADA")) {
            throw new TransaccionInversionException("No se puede cancelar una orden cerrada.");
        }

        ItemPortfolio item = orden.getItemPortfolio();

        // Devolvemos lo que quedaba bloqueado a libre
        item.setCantidadLibre(item.getCantidadLibre() + orden.getCantidadRestante());
        item.setCantidadBloqueada(item.getCantidadBloqueada() - orden.getCantidadRestante());

        orden.setCantidadRestante(0L);
        orden.setEstado("CANCELADA");

        ordenVentaRepository.save(orden);
    }
}