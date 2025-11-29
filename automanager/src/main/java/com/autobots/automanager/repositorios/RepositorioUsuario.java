package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entidades.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    // Corrigido: busca pelo nomeUsuario dentro da credencial
    Usuario findByCredencialNomeUsuario(String nomeUsuario);
}