package com.utnfrc.usuario_portfolios.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter

@Entity
@Table(name = "portfolios")
public class Portfolio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemPortfolio> items = new ArrayList<>();

    public void addAccion(Accion accion, Long cantidad) {
        ItemPortfolio item = new ItemPortfolio();
        item.setAccion(accion);
        item.setCantidad(cantidad);
        item.setPortfolio(this);
        this.items.add(item);
    }

}