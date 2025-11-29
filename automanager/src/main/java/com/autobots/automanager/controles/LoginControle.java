package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.jwt.ProvedorJwt;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class LoginControle {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;

    public static record LoginResponse(String token, String tipo, String perfis) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credencial credencial) {
        if (credencial == null || credencial.getNomeUsuario() == null || credencial.getSenha() == null) {
            return ResponseEntity.badRequest()
                                 .body("{\"erro\": \"Credenciais inválidas\"}");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    credencial.getNomeUsuario(),
                    credencial.getSenha()
                )
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = provedorJwt.proverJwt(userDetails.getUsername());
            String perfis = String.join(",", userDetails.getAuthorities().stream()
                                        .map(a -> a.getAuthority())
                                        .toList());

            return ResponseEntity.ok(new LoginResponse(token, "Bearer", perfis));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("{\"erro\": \"Credenciais inválidas\"}");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> usuarioLogado(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("{\"erro\": \"Não autenticado\"}");
        }

        return ResponseEntity.ok("{\"usuario\": \"" + authentication.getName() + "\"}");
    }
}
