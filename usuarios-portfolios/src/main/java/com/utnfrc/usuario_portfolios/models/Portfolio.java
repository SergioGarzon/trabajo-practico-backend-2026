package com.utnfrc.usuario_portfolios.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToOne(mappedBy = "portfolio")
    @JsonBackReference // Detiene la recursión infinita al serializar a JSON
    private Usuarios usuario;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemPortfolio> items = new ArrayList<>();

    public void addAccion(Accion accion, Long cantidadComprada) {
        ItemPortfolio item = new ItemPortfolio();
        item.setAccion(accion);
        item.setCantidadLibre(cantidadComprada);
        item.setCantidadBloqueada(0L);
        item.setPortfolio(this);
        this.items.add(item);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Usuarios getUsuario() { return usuario; }
    public void setUsuario(Usuarios usuario) { this.usuario = usuario; }
    public List<ItemPortfolio> getItems() { return items; }
    public void setItems(List<ItemPortfolio> items) { this.items = items; }

}