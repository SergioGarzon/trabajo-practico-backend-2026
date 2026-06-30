package com.utnfrc.usuario_portfolios.dtos;

import com.utnfrc.usuario_portfolios.models.Portfolio;
import com.utnfrc.usuario_portfolios.services.PortfolioService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter@ToString
@NoArgsConstructor
public class UsuarioDTO {
    private String Nombre;
    private String Apellido;
    private String username;
    private Double dineroTotal;
    private Double dineroLibre;
    private Double dineroBloqueado;
    private Double dineroInvertido;
    private Portfolio portfolio;
}
