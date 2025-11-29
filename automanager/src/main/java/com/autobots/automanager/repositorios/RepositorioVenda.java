package com.autobots.automanager.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;


public interface RepositorioVenda extends JpaRepository<Venda, Long> {
    List<Venda> findByCliente(Usuario cliente);
    List<Venda> findByFuncionario(Usuario funcionario);
}