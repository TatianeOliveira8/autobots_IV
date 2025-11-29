package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servicos")
public class ControleServico {

    @Autowired
    private RepositorioServico repositorio;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping("/cadastrar")
    public ResponseEntity<Servico> cadastrarServico(@RequestBody Servico servico) {
        if (servico == null || servico.getNome() == null || servico.getNome().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Servico salvo = repositorio.save(servico);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<Servico>> listarServicos() {
        List<Servico> lista = repositorio.findAll();
        return lista.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
            : new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Servico> obterServico(@PathVariable Long id) {
        return repositorio.findById(id)
            .map(servico -> new ResponseEntity<>(servico, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/atualizar")
    public ResponseEntity<Servico> atualizarServico(@RequestBody Servico servico) {
        if (servico.getId() == null || !repositorio.existsById(servico.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Servico atualizado = repositorio.save(servico);
        return new ResponseEntity<>(atualizado, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repositorio.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
