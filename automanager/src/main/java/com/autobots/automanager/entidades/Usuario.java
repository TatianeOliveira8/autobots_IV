package com.autobots.automanager.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.autobots.automanager.modelos.Perfil;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Credencial credencial;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Perfil> perfis = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Cliente cliente;

    public Usuario(String nome, Credencial credencial, List<Perfil> perfis) {
        this.nome = nome;
        this.credencial = credencial;
        this.perfis = perfis;
    }
}
