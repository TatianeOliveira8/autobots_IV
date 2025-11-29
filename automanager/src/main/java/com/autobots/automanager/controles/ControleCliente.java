package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioCliente;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/clientes")
public class ControleCliente {

    @Autowired
    private RepositorioCliente repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    private BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/me")
    public ResponseEntity<?> verMeuCadastro(Authentication authentication) {
        Usuario usuarioAutenticado = repositorioUsuario.findByCredencialNomeUsuario(authentication.getName());

        if (usuarioAutenticado == null) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.UNAUTHORIZED);
        }

        Cliente cliente = usuarioAutenticado.getCliente();

        if (cliente == null) {
            return new ResponseEntity<>("Cliente não encontrado para este usuário", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
        if (cliente.getUsuario() == null) {
            Usuario usuario = new Usuario();
            usuario.setNome(cliente.getNome());
            usuario.setPerfis(List.of(Perfil.ROLE_CLIENTE));

            Credencial cred = new Credencial();
            cred.setNomeUsuario(cliente.getNome().toLowerCase().replace(" ", ""));
            cred.setSenha(codificador.encode("senha123")); 
            usuario.setCredencial(cred);

            cliente.setUsuario(usuario);
        }

        repositorio.save(cliente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return new ResponseEntity<>(repositorio.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obterCliente(@PathVariable Long id) {
        return repositorio.findById(id)
            .map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
