# AutoManager - Sistema de Gestão para Oficinas Automotivas

## Descrição

Sistema de gestão para oficinas automotivas com microserviços, autenticação JWT e controle completo de empresas, clientes, funcionários, vendas, mercadorias, serviços e veículos.

---

## Funcionalidades Principais

Você constrói APIs para entregar as seguintes informações:

* Lista de todos os **clientes cadastrados por empresa**, com documentos, telefones, endereços e demais dados completos.
* Lista de todos os **funcionários cadastrados por empresa**, com documentos, perfil, telefones, endereços e outras informações associadas.
* Lista de **serviços e mercadorias disponíveis para venda por empresa**, incluindo data de cadastro, nome, descrição, valor etc.
* Lista de **todos os serviços ou peças vendidos por empresa** dentro de um intervalo de datas definido.
* Lista de **todos os veículos atendidos por empresa**, com seus dados completos.
---

## Segurança

* Autenticação JWT
* Perfis de acesso: ADMIN, GERENTE, VENDEDOR, CLIENTE
* Proteção de rotas com Spring Security
* Token expira em 10 minutos

---

## Tecnologias Utilizadas

**Backend:** Java 17, Spring Boot 2.7, Spring Data JPA, Spring Security, HATEOAS
**Banco:** MySQL 8 ou H2 em memória, Hibernate
**Segurança:** JWT (JJWT), BCrypt
**Ferramentas:** Maven, Lombok
**Testes:** JUnit, Spring Boot Test

---

## Como Executar

### Banco de Dados — MySQL

```sql
DROP DATABASE IF EXISTS base;
CREATE DATABASE base CHARACTER SET utf8mb4;
```

Configurar URL, usuário e senha no `application.properties`.

### Banco de Dados — H2

Usado por padrão. Console:

```
http://localhost:8080/h2-console
jdbc:h2:mem:automanagerdb
```

### Rodar os microserviços

**Sistema (8081):**

```
cd sistema
./mvnw spring-boot:run
```

**API (8080):**

```
cd api
./mvnw spring-boot:run
```

---

## Autenticação

### Login

```http
POST /login
{
  "nomeUsuario": "admin",
  "senha": "123456"
}
```

Header obrigatório para rotas protegidas:

```
Authorization: Bearer SEU_TOKEN
```
---

## Observações Importantes

* Token expira em 10 minutos
* Microserviço Sistema (8081) não usa JWT
* MySQL exige criação prévia da base
* H2 facilita testes rápidos
