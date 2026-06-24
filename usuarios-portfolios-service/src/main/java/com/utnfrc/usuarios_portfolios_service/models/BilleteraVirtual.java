package com.utnfrc.usuarios_portfolios_service.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "billeteras_virtuales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BilleteraVirtual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String alias;
    private Long numeroCBU;
    
    // REQUERIMIENTO FINANCIERO: BigDecimal para precisión decimal estricta en ARS
    @Column(name = "dinero_total", nullable = false)
    private BigDecimal dineroTotal = BigDecimal.ZERO;
    
    @Column(name = "dinero_libre", nullable = false)
    private BigDecimal dineroLibre = BigDecimal.ZERO;
    
    @Column(name = "dinero_invertido", nullable = false)
    private BigDecimal dineroInvertido = BigDecimal.ZERO;

    @OneToOne(mappedBy = "billeteraVirtual")
    @JsonBackReference
    private Usuario usuario;
}
