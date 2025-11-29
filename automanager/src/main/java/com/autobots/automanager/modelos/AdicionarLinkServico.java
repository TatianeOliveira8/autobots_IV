package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleServico;
import com.autobots.automanager.entidades.Servico;

@Component
public class AdicionarLinkServico {

    public void adicionarLink(Servico servico) {
        servico.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ControleServico.class)
                    .obterServico(servico.getId())
            ).withSelfRel()
        );

        servico.add(
        WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(ControleServico.class)
                .listarServicos()
        ).withRel("servicos")
    );

    }

    public void adicionarLink(List<Servico> servicos) {
        for (Servico servico : servicos) {
            adicionarLink(servico);
        }
    }
}
