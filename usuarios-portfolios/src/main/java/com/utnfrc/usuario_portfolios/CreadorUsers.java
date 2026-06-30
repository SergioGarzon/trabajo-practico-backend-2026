package com.utnfrc.usuario_portfolios;

import com.utnfrc.usuario_portfolios.dtos.RegistroDTO;
import com.utnfrc.usuario_portfolios.models.Usuarios;
import com.utnfrc.usuario_portfolios.services.IAccionService;
import com.utnfrc.usuario_portfolios.services.IPortfolioService;
import com.utnfrc.usuario_portfolios.services.IRegistroService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CreadorUsers {
    private final IRegistroService usuarioService;
    private final IAccionService accionService;
    private final IPortfolioService portfolioService;

    public CreadorUsers(IRegistroService usuarioService, IAccionService accionService, IPortfolioService portfolioService) {
        this.usuarioService = usuarioService;
        this.accionService = accionService;
        this.portfolioService = portfolioService;
    }

    public void iniciarPrograma() {
        // Esto crea las acciones en la base de datos
        accionService.sincronizarAcciones();

        Random random = new Random();

        // Datos de prueba para generar usuarios aleatorios
        String[] nombres = {"Juan", "Maria", "Carlos", "Lucia", "Pedro", "Ana", "Miguel", "Sofia", "Diego", "Laura"};
        String[] apellidos = {"Gomez", "Perez", "Rodriguez", "Fernandez", "Lopez", "Martinez", "Gonzalez", "Garcia", "Sanchez", "Romero"};
        String[] acciones = {"NVDA", "AAPL", "MSFT", "GOOGL", "AMZN"};

        // Comienza bucle para 10 usuarios
        for (int i = 0; i < 10; i++) {
            RegistroDTO registroDTO = new RegistroDTO();
            System.out.println("llego aca esta cantidad de veces: " + i);
            // Selección de nombre y apellido iterativa
            String nombre = nombres[i];
            String apellido = apellidos[i];

            registroDTO.setNombre(nombre);
            registroDTO.setApellido(apellido);
            // Agregamos el índice al final del email y username para garantizar que sean únicos
            registroDTO.setEmail(nombre.toLowerCase() + "." + apellido.toLowerCase() + i + "@ejemplo.com");
            registroDTO.setPassword("Pass" + (123));
            registroDTO.setRol("USER");
            registroDTO.setDni(20000000L + random.nextInt(20000000)); // DNI aleatorio entre 20M y 40M
            registroDTO.setUsername(nombre.toLowerCase() + apellido.toLowerCase() + i);
            registroDTO.setDomicilio("Calle Falsa " + (100 + random.nextInt(900)));
            System.out.println("llego aca esta cantidad de veces 2: " + i);
            Usuarios us = usuarioService.registrarUsuarioCompleto(registroDTO);
            System.out.println("llego aca esta cantidad de veces 3: " + i);
            // Generar un saldo aleatorio entre 100.000 y 1.000.000, redondeado a dos decimales
            double dineroAleatorio = 100000D + (900000D * random.nextDouble());
            us.getBilleteraVirtual().setDineroLibre(Math.round(dineroAleatorio * 100.0) / 100.0);

            // Elegir una acción aleatoria de la lista y comprar una cantidad entre 1 y 50
            String simboloAccion = acciones[random.nextInt(acciones.length)];
            long cantidadAcciones = 1L + random.nextInt(50);
            System.out.println(i);
            portfolioService.agregarAccion(us.getPortfolio().getId(), accionService.buscarPorSimbolo(simboloAccion), cantidadAcciones);
        }
        // Fin de bloque
    }
}
