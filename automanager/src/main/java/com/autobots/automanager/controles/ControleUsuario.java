package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuarios")
public class ControleUsuario {

    @Autowired
    private RepositorioUsuario repositorio;

    private BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario, Authentication auth) {
        Usuario logado = repositorio.findByCredencialNomeUsuario(auth.getName());

        if (logado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
            if (usuario.getPerfis().size() != 1 || !usuario.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
                return new ResponseEntity<>("VENDEDOR só pode criar usuários CLIENTE", HttpStatus.FORBIDDEN);
            }
        }

        if (logado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
            if (usuario.getPerfis().contains(Perfil.ROLE_ADMIN)) {
                return new ResponseEntity<>("GERENTE não pode criar ADMIN", HttpStatus.FORBIDDEN);
            }
        }

        Credencial cred = new Credencial();
        cred.setNomeUsuario(usuario.getCredencial().getNomeUsuario());
        cred.setSenha(codificador.encode(usuario.getCredencial().getSenha()));
        usuario.setCredencial(cred);

        repositorio.save(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios(Authentication auth) {
        Usuario logado = repositorio.findByCredencialNomeUsuario(auth.getName());
        List<Usuario> usuarios = repositorio.findAll();
        if (logado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
            usuarios.removeIf(u -> !u.getPerfis().contains(Perfil.ROLE_CLIENTE));
        }
        if (logado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
            usuarios.removeIf(u -> u.getPerfis().contains(Perfil.ROLE_ADMIN));
        }

        return new ResponseEntity<>(usuarios, usuarios.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id, Authentication auth) {
        Usuario logado = repositorio.findByCredencialNomeUsuario(auth.getName());
        Optional<Usuario> usuarioOpt = repositorio.findById(id);

        if (usuarioOpt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Usuario usuario = usuarioOpt.get();
        if (logado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
            if (!usuario.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (logado.getPerfis().contains(Perfil.ROLE_GERENTE) &&
            usuario.getPerfis().contains(Perfil.ROLE_ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario novosDados, Authentication auth) {
        Optional<Usuario> opt = repositorio.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Usuario usuario = opt.get();
        Usuario logado = repositorio.findByCredencialNomeUsuario(auth.getName());

        if (logado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
            if (!usuario.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
                return new ResponseEntity<>("VENDEDOR só pode atualizar usuários CLIENTE", HttpStatus.FORBIDDEN);
            }
            if (novosDados.getPerfis().size() != 1 || !novosDados.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
                return new ResponseEntity<>("VENDEDOR não pode alterar perfil para não-CLIENTE", HttpStatus.FORBIDDEN);
            }
        }

        if (logado.getPerfis().contains(Perfil.ROLE_GERENTE) &&
            usuario.getPerfis().contains(Perfil.ROLE_ADMIN)) {
            return new ResponseEntity<>("GERENTE não pode atualizar ADMIN", HttpStatus.FORBIDDEN);
        }

        usuario.setNome(novosDados.getNome());
        usuario.setPerfis(novosDados.getPerfis());
        repositorio.save(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id, Authentication auth) {
        Optional<Usuario> opt = repositorio.findById(id);
        if (opt.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Usuario usuario = opt.get();
        Usuario logado = repositorio.findByCredencialNomeUsuario(auth.getName());

        if (logado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
            if (!usuario.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
                return new ResponseEntity<>("VENDEDOR só pode excluir usuários CLIENTE", HttpStatus.FORBIDDEN);
            }
        }

        if (logado.getPerfis().contains(Perfil.ROLE_GERENTE) &&
            usuario.getPerfis().contains(Perfil.ROLE_ADMIN)) {
            return new ResponseEntity<>("GERENTE não pode excluir ADMIN", HttpStatus.FORBIDDEN);
        }

        repositorio.delete(usuario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
