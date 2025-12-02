# AutoBots - Gestão de Usuários, Vendas, Serviços e Mercadorias

## Estrutura e funcionalidades

O AutoBots é um sistema voltado para gerenciamento de usuários, vendas, serviços e mercadorias, com autenticação via JWT e controle de permissões por perfil.

Funcionalidades principais:

* CRUD completo de usuários, vendas, serviços e mercadorias.
* Autenticação e autorização com **Json Web Token (JWT)**.
* Perfis de usuários: Administrador, Gerente, Vendedor e Cliente.
* Arquitetura seguindo princípios **SOLID**.

---

## 📝 Rotas disponíveis

### 🔓 AUTENTICAÇÃO (Público)

* **POST** `http://localhost:8080/auth/login` - Fazer login
* **GET** `http://localhost:8080/auth/me` - Verificar usuário autenticado

### 👤 USUÁRIOS

* **GET** `http://localhost:8080/usuarios/listar` - Lista todos (filtrado por perfil)
* **GET** `http://localhost:8080/usuarios/{id}` - Busca por ID
* **POST** `http://localhost:8080/usuarios/cadastrar` - Criar
* **PUT** `http://localhost:8080/usuarios/atualizar/{id}` - Atualizar
* **DELETE** `http://localhost:8080/usuarios/excluir/{id}` - Excluir

### 💰 VENDAS

* **GET** `http://localhost:8080/vendas` - Lista todas (filtrado por perfil)
* **GET** `http://localhost:8080/vendas/{id}` - Busca por ID
* **POST** `http://localhost:8080/vendas/cadastrar` - Criar
* **PUT** `http://localhost:8080/vendas/atualizar` - Atualizar
* **DELETE** `http://localhost:8080/vendas/deletar/{id}` - Excluir

### 🛠️ SERVIÇOS

* **GET** `http://localhost:8080/servicos/listar` - Lista todos
* **GET** `http://localhost:8080/servicos/{id}` - Busca por ID
* **POST** `http://localhost:8080/servicos/cadastrar` - Criar
* **PUT** `http://localhost:8080/servicos/atualizar` - Atualizar
* **DELETE** `http://localhost:8080/servicos/deletar/{id}` - Excluir

### 📦 MERCADORIAS

* **GET** `http://localhost:8080/mercadorias/listar` - Lista todas
* **GET** `http://localhost:8080/mercadorias/{id}` - Busca por ID
* **POST** `http://localhost:8080/mercadorias/cadastrar` - Criar
* **PUT** `http://localhost:8080/mercadorias/atualizar` - Atualizar
* **DELETE** `http://localhost:8080/mercadorias/deletar/{id}` - Excluir

---

## Exemplo de JSON para teste rápido - http://localhost:8080/usuarios/cadastrar

```json
{
  "nome": "teste",
  "credencial": {
    "nomeUsuario": "teste",
    "senha": "123456"
  },
  "perfis": ["ROLE_ADMIN"]
}
```

---

## Ambiente de Teste

* **Java:** 17
* **Framework:** Spring Boot
* **IDE:** Eclipse ou VS Code
* **MySQL:** 8.0 

⚠️ IMPORTANTE:
Se o banco 'base' não existir, a aplicação não inicia. 
Antes de rodar:
1. Acesse o MySQL.
2. Execute:
   DROP DATABASE IF EXISTS base;
   CREATE DATABASE base CHARACTER SET utf8mb4;
3. Depois inicie a aplicação.
