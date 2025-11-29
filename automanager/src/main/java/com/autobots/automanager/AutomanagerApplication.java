package com.autobots.automanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

	@Autowired
	private RepositorioUsuario repositorio;

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		
		// Criar usuário ADMIN
		Usuario admin = new Usuario();
		admin.setNome("Administrador");
		admin.getPerfis().add(Perfil.ROLE_ADMIN);
		Credencial credencialAdmin = new Credencial();
		credencialAdmin.setNomeUsuario("admin");
		credencialAdmin.setSenha(codificador.encode("123456"));
		admin.setCredencial(credencialAdmin);
		repositorio.save(admin);
		
		// Criar usuário CLIENTE com relacionamento bidirecional
		Usuario usuarioCliente = new Usuario();
		usuarioCliente.setNome("João Silva");
		usuarioCliente.getPerfis().add(Perfil.ROLE_CLIENTE);
		Credencial credencialCliente = new Credencial();
		credencialCliente.setNomeUsuario("cliente");
		credencialCliente.setSenha(codificador.encode("123456"));
		usuarioCliente.setCredencial(credencialCliente);
		
		// Criar Cliente e estabelecer relacionamento bidirecional
		Cliente cliente = new Cliente();
		cliente.setNome("João Silva");
		cliente.setUsuario(usuarioCliente);
		usuarioCliente.setCliente(cliente);
		
		// Salvar (cascade irá salvar o Cliente também)
		repositorio.save(usuarioCliente);
		
		// Criar usuário VENDEDOR
		Usuario vendedor = new Usuario();
		vendedor.setNome("Maria Vendedora");
		vendedor.getPerfis().add(Perfil.ROLE_VENDEDOR);
		Credencial credencialVendedor = new Credencial();
		credencialVendedor.setNomeUsuario("vendedor");
		credencialVendedor.setSenha(codificador.encode("123456"));
		vendedor.setCredencial(credencialVendedor);
		repositorio.save(vendedor);
		
		// Criar usuário GERENTE
		Usuario gerente = new Usuario();
		gerente.setNome("Carlos Gerente");
		gerente.getPerfis().add(Perfil.ROLE_GERENTE);
		Credencial credencialGerente = new Credencial();
		credencialGerente.setNomeUsuario("gerente");
		credencialGerente.setSenha(codificador.encode("123456"));
		gerente.setCredencial(credencialGerente);
		repositorio.save(gerente);
	}
}