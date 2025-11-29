package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/vendas")
public class ControleVenda {

    @Autowired
    private RepositorioVenda repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR','CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<Venda> obterVenda(@PathVariable Long id, Authentication authentication) {
        Venda venda = repositorio.findById(id).orElse(null);
        if (venda == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Usuario usuarioAutenticado = repositorioUsuario.findByCredencialNomeUsuario(authentication.getName());

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            if (!venda.getCliente().getId().equals(usuarioAutenticado.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDEDOR"))) {
            if (!venda.getFuncionario().getId().equals(usuarioAutenticado.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(venda, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('CLIENTE', 'VENDEDOR')")
    @GetMapping("/minhas")
    public ResponseEntity<List<Venda>> verMinhasVendas(Authentication authentication) {
        Usuario usuarioAutenticado = repositorioUsuario.findByCredencialNomeUsuario(authentication.getName());
        
        if (usuarioAutenticado == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        List<Venda> minhasVendas;
        
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            minhasVendas = repositorio.findByCliente(usuarioAutenticado);
        }
        else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDEDOR"))) {
            minhasVendas = repositorio.findByFuncionario(usuarioAutenticado);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        if (minhasVendas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(minhasVendas, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR','CLIENTE')")
    @GetMapping
    public ResponseEntity<List<Venda>> listarVendas(Authentication authentication) {
        List<Venda> lista = repositorio.findAll();
        Usuario usuarioAutenticado = repositorioUsuario.findByCredencialNomeUsuario(authentication.getName());

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
            lista = lista.stream()
                         .filter(v -> v.getCliente().getId().equals(usuarioAutenticado.getId()))
                         .collect(Collectors.toList());
        }

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDEDOR"))) {
            lista = lista.stream()
                         .filter(v -> v.getFuncionario().getId().equals(usuarioAutenticado.getId()))
                         .collect(Collectors.toList());
        }

        if (lista.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<Venda> cadastrarVenda(@RequestBody Venda venda, Authentication authentication) {
        if (venda.getId() != null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        Usuario usuarioAutenticado = repositorioUsuario.findByCredencialNomeUsuario(authentication.getName());

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDEDOR"))) {
            if (!venda.getFuncionario().getId().equals(usuarioAutenticado.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        Venda salva = repositorio.save(venda);
        return new ResponseEntity<>(salva, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/atualizar")
    public ResponseEntity<Venda> atualizarVenda(@RequestBody Venda venda) {
        if (venda.getId() == null || !repositorio.existsById(venda.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Venda atualizada = repositorio.save(venda);
        return new ResponseEntity<>(atualizada, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarVenda(@PathVariable Long id) {
        if (!repositorio.existsById(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        repositorio.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
