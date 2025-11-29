package com.autobots.automanager.entidades;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    @JsonBackReference
    private Usuario usuario;

    public Cliente(String nome, Usuario usuario) {
        this.nome = nome;
        this.usuario = usuario;
    }
}
