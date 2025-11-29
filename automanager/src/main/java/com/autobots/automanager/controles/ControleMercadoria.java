package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping("/mercadorias")
public class ControleMercadoria {

    @Autowired
    private RepositorioMercadoria repositorio;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        try {
            if (mercadoria == null || mercadoria.getNome() == null || mercadoria.getNome().isBlank()) {
                return new ResponseEntity<>("Nome é obrigatório", HttpStatus.BAD_REQUEST);
            }
            
            if (mercadoria.getCadastro() == null) {
                mercadoria.setCadastro(new Date());
            }
            if (mercadoria.getFabricao() == null) {
                mercadoria.setFabricao(new Date());
            }
            if (mercadoria.getValidade() == null) {
                Date umAnoDepois = new Date(mercadoria.getFabricao().getTime() + (365L * 24 * 60 * 60 * 1000));
                mercadoria.setValidade(umAnoDepois);
            }
            if (mercadoria.getQuantidade() == 0) {
                mercadoria.setQuantidade(1);
            }
            
            Mercadoria salva = repositorio.save(mercadoria);
            return new ResponseEntity<>(salva, HttpStatus.CREATED);
            
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar mercadoria: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao cadastrar: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<Mercadoria>> listarMercadorias() {
        List<Mercadoria> lista = repositorio.findAll();
        return lista.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
            : new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(mercadoria -> new ResponseEntity<>(mercadoria, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/atualizar")
    public ResponseEntity<Mercadoria> atualizarMercadoria(@RequestBody Mercadoria mercadoria) {
        if (mercadoria.getId() == null || !repositorio.existsById(mercadoria.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Mercadoria atualizada = repositorio.save(mercadoria);
        return new ResponseEntity<>(atualizada, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarMercadoria(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repositorio.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
