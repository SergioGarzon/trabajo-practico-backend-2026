package com.utnfrc.usuario_portfolios.services;

import com.utnfrc.usuario_portfolios.dtos.SolicitudDineroDTO;
import com.utnfrc.usuario_portfolios.excepciones.BilleteraExistenteException;
import com.utnfrc.usuario_portfolios.excepciones.BilleteraVituralNoExisteException;
import com.utnfrc.usuario_portfolios.models.BilleteraVirtual;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.repositories.BilleteraVirtualRepository;
import com.utnfrc.usuario_portfolios.repositories.UsuariosRepositories;
import jakarta.transaction.Transactional;
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

    public BilleteraVirtualService(UsuariosServices usuariosService, BilleteraVirtualRepository bvRepository, UsuariosRepositories usuariosRepositories) {
        this.usuariosRepositories = usuariosRepositories;
        this.usuariosService = usuariosService;
        this.bvRepository = bvRepository;
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



    @Transactional
    public boolean solicitarYBloquearDinero(String keycloakUserId, Long monto) {
        BilleteraVirtual bv = bvRepository.findByUsuario_Id(keycloakUserId)
                .orElseThrow(() -> new BilleteraExistenteException("Billetera no encontrada"));

        // 1. Validamos si tiene fondos suficientes en el dinero LIBRE
        if (bv.getDineroLibre() < monto) {
            return false;
        }

        bv.setDineroLibre(bv.getDineroLibre() - monto);

        Long actualBloqueado = bv.getDineroBloqueado() != null ? bv.getDineroBloqueado() : 0L;
        bv.setDineroBloqueado(actualBloqueado + monto);

        bvRepository.save(bv);
        return true;
    }

    @Transactional
    public BilleteraVirtual procesarRespuestaExterna(String userId, SolicitudDineroDTO dto) {
        BilleteraVirtual bv = bvRepository.findByUsuario_Id(userId)
                .orElseThrow(() -> new BilleteraExistenteException("Billetera no encontrada"));

        Long monto = dto.getMonto();

        // Control de seguridad: Validamos que realmente tengamos dinero bloqueado suficiente para procesar
        if (bv.getDineroBloqueado() < monto) {
            throw new IllegalArgumentException("No hay suficiente dinero bloqueado para procesar esta acción");
        }

        // Desbloqueamos el dinero del limbo primero
        bv.setDineroBloqueado(bv.getDineroBloqueado() - monto);

        if ("CONFIRMAR".equalsIgnoreCase(dto.getEstadoAccion())) {
            // CASO A: Compra afirmativa -> Pasa a dinero invertido
            bv.setDineroInvertido(bv.getDineroInvertido() + monto);
        } else if ("RECHAZAR".equalsIgnoreCase(dto.getEstadoAccion())) {
            // CASO B: Compra rechazada -> Se devuelve al dinero libre
            bv.setDineroLibre(bv.getDineroLibre() + monto);
        } else {
            throw new IllegalArgumentException("Estado de acción no reconocido");
        }

        return bvRepository.save(bv);
    }

}
