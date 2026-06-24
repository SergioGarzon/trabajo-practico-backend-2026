package com.utnfrc.usuarios_portfolios_service.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "portfolio_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"portfolio_id", "symbol"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String symbol; // Símbolo bursátil (Ej: NVDA, AAPL, TSLA)

    @Column(nullable = false)
    private Integer shares = 0; // Cantidad de acciones (shares) que posee
}
