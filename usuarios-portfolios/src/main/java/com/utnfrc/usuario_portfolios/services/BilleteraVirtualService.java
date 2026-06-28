package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.dtos.SolicitudDineroDTO;
import com.utnfrc.usuario_portfolios.excepciones.*;
import com.utnfrc.usuario_portfolios.models.*;
import com.utnfrc.usuario_portfolios.repositories.BilleteraVirtualRepository;
import com.utnfrc.usuario_portfolios.repositories.ReservaSaldoRepository;
import com.utnfrc.usuario_portfolios.repositories.UsuariosRepositories;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class BilleteraVirtualService implements IBilleteraVirtualService {

    private final UsuariosServices usuariosService;
    private final BilleteraVirtualRepository bvRepository;
    private final SecureRandom random = new SecureRandom();
    private final UsuariosRepositories usuariosRepositories;
    private final ReservaSaldoRepository reservaRepository;
    private final PortfolioService portfolioService;

    @Autowired
    private AccionService accionService;

    public BilleteraVirtualService(UsuariosServices usuariosService, PortfolioService portfolioService, BilleteraVirtualRepository bvRepository, UsuariosRepositories usuariosRepositories, ReservaSaldoRepository reservaRepository) {
        this.usuariosRepositories = usuariosRepositories;
        this.usuariosService = usuariosService;
        this.bvRepository = bvRepository;
        this.reservaRepository = reservaRepository;
        this.portfolioService = portfolioService;
    }

    @Transactional
    public BilleteraVirtual createBV(String usuarioID) {
        Usuarios usuario = usuariosService
                .getById(usuarioID).orElseThrow(() -> new BilleteraVituralException("Usuario no encontrado"));

        if (usuario.getBilleteraVirtual() != null) {
            throw new BilleteraVituralException("El usuario ya tiene una billetera asociada");
        }

        BilleteraVirtual newBV = new BilleteraVirtual();
        Long numeroCBU = Math.abs(random.nextLong() % 1_000_000_000_000L); // 12 dígitos aprox.
        newBV.setNumeroCBU(numeroCBU);
        newBV.setAlias(usuario.getNombre() + " " + usuario.getApellido());
        newBV.setDineroLibre(0L);
        newBV.setDineroInvertido(0L);
        newBV.setDineroBloqueado(0L);
        newBV.setUsuario(usuario);
        usuario.setBilleteraVirtual(newBV);
        usuariosRepositories.save(usuario);
        return newBV;
    }

    public List<BilleteraVirtual> findAllBV() {
        return bvRepository.findAll();
    }

    public Optional<BilleteraVirtual> findBVById(Long id) {
        return bvRepository.findById(id);
    }

    public void deleteBV(Long dni) {
        bvRepository.deleteById(dni);
    }


    @Transactional
    public BilleteraVirtual retirarDinero(String idUsuario, Long cantidad) {

        BilleteraVirtual bv = bvRepository.findByUsuario_Id(idUsuario)
                .orElseThrow(() -> new BilleteraVituralException("La billetera no fue encontrada"));

        bv.retirarDinero(cantidad);
        bvRepository.save(bv);
        return bv;
    }

    @Transactional
    public BilleteraVirtual ingresarDinero(String idUsuario, Long cantidad) {
        BilleteraVirtual bv = bvRepository.findByUsuario_Id(idUsuario)
                .orElseThrow(() -> new BilleteraVituralException("La billetera no fue encontrada"));
        bv.ingresarDinero(cantidad);
        bvRepository.save(bv);
        return bv;
    }


    @Transactional
    public String solicitarYBloquearDinero(String keycloakUserId, Long monto, String idOrdenCompra) {
        BilleteraVirtual bv = bvRepository.findByUsuario_Id(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Billetera no encontrada"));

        if (bv.getDineroLibre() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la billetera virtual.");
        }

        bv.setDineroLibre(bv.getDineroLibre() - monto);
        bv.setDineroBloqueado((bv.getDineroBloqueado() != null ? bv.getDineroBloqueado() : 0L) + monto);
        bvRepository.save(bv);

        OrdenCompra reserva = new OrdenCompra(idOrdenCompra);
        reserva.setMonto(monto);
        reserva.setBilletera(bv);

        reservaRepository.save(reserva);

        return reserva.getId();
    }


    @Transactional
    public BilleteraVirtual procesarRespuestaExterna(String userID, SolicitudDineroDTO dto) {

        OrdenCompra reserva = reservaRepository.findById(dto.getIdOrdenCompra())
                .orElseThrow(() -> new TransaccionInversionException("La transacción de bloqueo no existe."));

        Optional<Usuarios> user = usuariosService.getById(userID);
        if (user.isEmpty()) {throw new BilleteraVituralException("Usuario no encontrado");}
        BilleteraVirtual bv = reserva.getBilletera();
        Long monto = reserva.getMonto();

        bv.setDineroBloqueado(bv.getDineroBloqueado() - monto);

        if ("CONFIRMAR".equalsIgnoreCase(dto.getEstadoAccion())) {
            bv.setDineroInvertido(bv.getDineroInvertido() + monto);
            Portfolio portID = user.get().getPortfolio();
            if (portID == null) {throw new TransaccionInversionException("El portfolio no fue encontrado");}
            Accion accion = accionService.buscarPorSimbolo(dto.getSimbolo());
            if (accion == null) {throw new TransaccionInversionException("No se encontro la accion");}
            portfolioService.agregarAccion(portID.getId(), accion, dto.getCantidad());
            reserva.setEstado("CONFIRMADA");
        } else if ("RECHAZAR".equalsIgnoreCase(dto.getEstadoAccion())) {
            bv.setDineroLibre(bv.getDineroLibre() + monto);
            reserva.setEstado("RECHAZADA");
        } else {
            throw new IllegalArgumentException("Accion no reconocida.");
        }

        reservaRepository.save(reserva);
        return bvRepository.save(bv);
    }

}
