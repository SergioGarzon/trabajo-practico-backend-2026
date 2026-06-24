package com.utnfrc.usuarios_portfolios_service.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utnfrc.usuarios_portfolios_service.models.BilleteraVirtual;
import com.utnfrc.usuarios_portfolios_service.models.Portfolio;
import com.utnfrc.usuarios_portfolios_service.models.Usuario;
import com.utnfrc.usuarios_portfolios_service.repositories.IUsuariosRepositories;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
    private IUsuariosRepositories usuariosRepository;

    @Override
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuario.getBilleteraVirtual() == null) {
            BilleteraVirtual nuevaBilletera = new BilleteraVirtual();
            nuevaBilletera.setAlias(usuario.getNombre().toLowerCase() + "." + usuario.getApellido().toLowerCase());
            nuevaBilletera.setNumeroCBU(1000000000L + usuario.getDni()); // Simulación de CBU único
            nuevaBilletera.setDineroTotal(BigDecimal.ZERO);
            nuevaBilletera.setDineroLibre(BigDecimal.ZERO);
            nuevaBilletera.setDineroInvertido(BigDecimal.ZERO);
            nuevaBilletera.setUsuario(usuario);
            usuario.setBilleteraVirtual(nuevaBilletera);
        }

        if (usuario.getPortfolio() == null) {
            Portfolio nuevoPortfolio = new Portfolio();
            nuevoPortfolio.setDescripcion("Portfolio de inversiones de " + usuario.getNombre());
            usuario.setPortfolio(nuevoPortfolio);
        }

        return usuariosRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorDni(Long dni) {
        return usuariosRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Usuario con DNI " + dni + " no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorKeycloakId(String keycloakId) {
        return usuariosRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Usuario con Keycloak ID " + keycloakId + " no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuariosRepository.findAll();
    }
}
