package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.dtos.SolicitudDineroDTO;
import com.utnfrc.usuario_portfolios.excepciones.*;
import com.utnfrc.usuario_portfolios.models.*;
import com.utnfrc.usuario_portfolios.repositories.BilleteraVirtualRepository;
import com.utnfrc.usuario_portfolios.repositories.ReservaSaldoRepository;
import com.utnfrc.usuario_portfolios.repositories.UsuariosRepositories;
import com.utnfrc.usuario_portfolios.services.AccionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class BilleteraVirtualService {

    private final UsuariosServices usuariosService;
    private final BilleteraVirtualRepository bvRepository;
    private final SecureRandom random = new SecureRandom();
    private final UsuariosRepositories usuariosRepositories;
    private final ReservaSaldoRepository reservaRepository;

    @Autowired
    private AccionService accionService;

    public BilleteraVirtualService(UsuariosServices usuariosService, BilleteraVirtualRepository bvRepository, UsuariosRepositories usuariosRepositories, ReservaSaldoRepository reservaRepository) {
        this.usuariosRepositories = usuariosRepositories;
        this.usuariosService = usuariosService;
        this.bvRepository = bvRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public BilleteraVirtual createBV(String usuarioID) {
        Usuarios usuario = usuariosService
                .getById(usuarioID).orElseThrow(() -> new BilleteraVituralNoExisteException("Usuario no encontrado"));

        if (usuario.getBilleteraVirtual() != null) {
            throw new BilleteraExistenteException("El usuario ya tiene una billetera asociada");
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
                .orElseThrow(() -> new BilleteraVituralNoExisteException("La billetera no fue encontrada"));

        bv.retirarDinero(cantidad);
        bvRepository.save(bv);
        return bv;
    }

    @Transactional
    public BilleteraVirtual ingresarDinero(String idUsuario, Long cantidad) {
        BilleteraVirtual bv = bvRepository.findByUsuario_Id(idUsuario)
                .orElseThrow(() -> new BilleteraVituralNoExisteException("La billetera no fue encontrada"));
        bv.ingresarDinero(cantidad);
        bvRepository.save(bv);
        return bv;
    }



//    @Transactional
//    public boolean solicitarYBloquearDinero(String keycloakUserId, Long monto) {
//        BilleteraVirtual bv = bvRepository.findByUsuario_Id(keycloakUserId)
//                .orElseThrow(() -> new BilleteraExistenteException("Billetera no encontrada"));
//
//        // 1. Validamos si tiene fondos suficientes en el dinero LIBRE
//        if (bv.getDineroLibre() < monto) {
//            return false;
//        }
//
//        bv.setDineroLibre(bv.getDineroLibre() - monto);
//
//        Long actualBloqueado = bv.getDineroBloqueado() != null ? bv.getDineroBloqueado() : 0L;
//        System.out.println(actualBloqueado + " AaaAAAAAAAAAAAAA");
//        bv.setDineroBloqueado(actualBloqueado + monto);
//
//        bvRepository.save(bv);
//        return true;
//    }

    @Transactional
    public String solicitarYBloquearDinero(String keycloakUserId, Long monto) {
        BilleteraVirtual bv = bvRepository.findByUsuario_Id(keycloakUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Billetera no encontrada"));

        if (bv.getDineroLibre() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la billetera virtual.");
        }

        // 1. Restamos del dinero libre y sumamos al bloqueado general
        bv.setDineroLibre(bv.getDineroLibre() - monto);
        bv.setDineroBloqueado((bv.getDineroBloqueado() != null ? bv.getDineroBloqueado() : 0L) + monto);
        bvRepository.save(bv);

        // 2. Creamos el registro del Ledger con su ID único
        ReservaSaldo reserva = new ReservaSaldo();
        reserva.setMonto(monto);
        reserva.setBilletera(bv);

        reservaRepository.save(reserva); // Asumiendo que creás este JpaRepository

        return reserva.getId(); // Retornamos el UUID generado
    }


//    @Transactional
//    public BilleteraVirtual procesarRespuestaExterna(String userId, SolicitudDineroDTO dto) {
//        BilleteraVirtual bv = bvRepository.findByUsuario_Id(userId)
//                .orElseThrow(() -> new BilleteraExistenteException("Billetera no encontrada"));
//
//        Long monto = dto.getMonto();
//        Accion acc = accionService.buscarPorSimbolo(dto.getSimbolo());
//        if (acc == null) {throw new TransaccionInversionException("Accion no encontrada");}
//
//
//        // Control de seguridad: Validamos que realmente tengamos dinero bloqueado suficiente para procesar
//        if (bv.getDineroBloqueado() < monto) {
//            throw new IllegalArgumentException("No hay suficiente dinero bloqueado para procesar esta acción");
//        }
//
//        // Desbloqueamos el dinero del limbo primero
//        bv.setDineroBloqueado(bv.getDineroBloqueado() - monto);
//
//        Portfolio pf = bv.getUsuario().getPortfolio();
//
//
//        if ("CONFIRMAR".equalsIgnoreCase(dto.getEstadoAccion())) {
//            // CASO A: Compra afirmativa -> Pasa a dinero invertido
//            bv.setDineroInvertido(bv.getDineroInvertido() + monto);
//            pf.addAccion(acc, dto.getCantidad());
//        } else if ("RECHAZAR".equalsIgnoreCase(dto.getEstadoAccion())) {
//            // CASO B: Compra rechazada -> Se devuelve al dinero libre
//            bv.setDineroLibre(bv.getDineroLibre() + monto);
//        } else {
//            throw new IllegalArgumentException("Estado de acción no reconocido");
//        }
//
//        return bvRepository.save(bv);
//    }

    @Transactional
    public BilleteraVirtual procesarRespuestaExterna(SolicitudDineroDTO dto) {
        // 1. Buscamos la transacción específica por su ID único. Así evitamos liberar cualquier cosa.
        ReservaSaldo reserva = reservaRepository.findById(dto.getIdTransaccion())
                .orElseThrow(() -> new ResourceNotFoundException("La transacción de bloqueo no existe."));

        if (!"PENDIENTE".equals(reserva.getEstado())) {
            throw new IllegalStateException("Esta transacción ya fue procesada anteriormente.");
        }

        BilleteraVirtual bv = reserva.getBilletera();
        Long monto = reserva.getMonto();

        // 2. Desbloqueamos el dinero del limbo general
        bv.setDineroBloqueado(bv.getDineroBloqueado() - monto);

        if ("CONFIRMAR".equalsIgnoreCase(dto.getEstadoAccion())) {
            bv.setDineroInvertido(bv.getDineroInvertido() + monto);
            reserva.setEstado("CONFIRMADA");
        } else if ("RECHAZAR".equalsIgnoreCase(dto.getEstadoAccion())) {
            bv.setDineroLibre(bv.getDineroLibre() + monto);
            reserva.setEstado("RECHAZADA");
        } else {
            throw new IllegalArgumentException("Accion militar no reconocida.");
        }

        reservaRepository.save(reserva);
        return bvRepository.save(bv);
    }

}
