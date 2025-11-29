package com.autobots.automanager.entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, exclude = { "cliente", "funcionario" })
@Entity
public class Venda extends RepresentationModel<Venda> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date cadastro;
    @Column(nullable = false, unique = true)
    private String identificacao;
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario cliente;
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario funcionario;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Mercadoria> mercadorias = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Servico> servicos = new HashSet<>();
}
