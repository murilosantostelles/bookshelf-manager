# 📚 Bookshelf Manager

Sistema web de gerenciamento de acervo pessoal de livros com controle de empréstimos. Projeto desenvolvido para uso real por uma psicóloga que precisava rastrear quais livros estavam em sua casa e quais estavam emprestados para outras pessoas.

🌐 **[API em produção](https://bookshelf-manager-production.up.railway.app/swagger-ui/index.html)** — documentação interativa via Swagger

---

## 🎯 Sobre o Projeto

A psicóloga possuía um acervo grande de livros e frequentemente perdia o controle de quais estavam emprestados, para quem e desde quando. O Bookshelf Manager resolve esse problema oferecendo um sistema completo de cadastro de livros, gestão de empréstimos e busca por múltiplos critérios — tudo isolado por usuário autenticado, permitindo que qualquer pessoa crie sua conta e gerencie seu próprio acervo.

---

## ✨ Funcionalidades

- Cadastro, edição e remoção de livros com capa, sinopse, autor, editora, categoria e palavras-chave
- Busca de livros por título, autor, categoria, editora e palavras-chave
- Controle de empréstimos com nome da pessoa, data de empréstimo e data de devolução
- Status automático do livro (Disponível / Emprestado)
- Autenticação com JWT — cada usuário acessa apenas seu próprio acervo
- Cache nos endpoints de listagem para otimização de performance
- Validação de regras de negócio (ex: não é possível emprestar um livro já emprestado)
- Pipeline de CI/CD com execução automática de testes e deploy contínuo

---

## 🗄️ Modelagem do Banco de Dados

Antes de iniciar o desenvolvimento, o banco de dados foi modelado no StarUML seguindo boas práticas de modelagem relacional, definindo entidades, atributos, chaves primárias, chaves estrangeiras e relacionamentos.

<img width="1072" height="598" alt="image" src="https://github.com/user-attachments/assets/dcb9c075-40b5-47a7-a54e-8a563831dfaa" />

---

## 🛠️ Tecnologias Utilizadas

**Backend**
- Java 21
- Spring Boot 4
- Spring MVC
- Spring Data JPA
- Spring Security + JWT
- Hibernate
- Flyway
- Caffeine Cache
- Lombok
- Bean Validation
- SpringDoc OpenAPI (Swagger)

**Banco de Dados**
- PostgreSQL
- Docker / Docker Compose

**Testes**
- JUnit 5
- Mockito

**CI/CD**
- GitHub Actions (CI — testes automáticos a cada push)
- Railway (CD — deploy automático após testes passarem)

**Deploy**
- Railway

**Frontend** *(em desenvolvimento)*
- React.js + Vite

---

## 📐 Arquitetura

O projeto segue a arquitetura MVC com separação clara de responsabilidades:

```
Controller  →  Service  →  Repository  →  Database
    ↑               ↑
  DTOs           Entities
```

- **Controller** — recebe as requisições HTTP e delega para o Service
- **Service** — contém as regras de negócio
- **Repository** — comunicação com o banco de dados via Spring Data JPA
- **DTOs** — objetos de transferência de dados, separando o que entra e o que sai da API
- **Entities** — mapeamento das tabelas do banco

---

## 🔐 Segurança

A API utiliza autenticação stateless com JWT. O fluxo é:

1. Usuário se registra em `POST /auth/register`
2. Usuário faz login em `POST /auth/login` e recebe um token JWT
3. Todas as requisições seguintes devem incluir o header `Authorization: Bearer <token>`
4. Cada usuário acessa exclusivamente seus próprios livros e empréstimos

---

## 📋 Endpoints Principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/auth/register` | Cadastro de usuário |
| POST | `/auth/login` | Login e geração de token |
| GET | `/livros` | Listar livros do usuário |
| POST | `/livros` | Cadastrar livro |
| PUT | `/livros/{id}` | Editar livro |
| DELETE | `/livros/{id}` | Remover livro |
| GET | `/livros/titulo?titulo=` | Buscar por título |
| GET | `/livros/autor?nome=` | Buscar por autor |
| GET | `/livros/categoria?nome=` | Buscar por categoria |
| GET | `/livros/palavraChave?palavra=` | Buscar por palavra-chave |
| POST | `/emprestimos` | Registrar empréstimo |
| DELETE | `/emprestimos/{id}` | Registrar devolução |
| GET | `/autores` | Listar autores |
| GET | `/categorias` | Listar categorias |

A documentação completa está disponível via Swagger em `/swagger-ui/index.html`.

---

## 🚀 Como Rodar Localmente

**Pré-requisitos:** Java 21, Docker, Maven

**1. Clone o repositório**
```bash
git clone https://github.com/murilosantostelles/bookshelf-manager.git
cd bookshelf-manager
```

**2. Suba o banco de dados com Docker**
```bash
docker compose up -d
```

**3. Configure as variáveis de ambiente**

As variáveis já possuem valores default para desenvolvimento local no `application.yml`. Para customizar, configure:
```
JWT_KEY=sua-chave-secreta-aqui
JWT_EXPIRATION=900000
```

**4. Rode a aplicação**
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

---

## 🧪 Testes

```bash
mvn test
```

Os testes unitários cobrem os Services com JUnit 5 e Mockito, validando regras de negócio como:
- Impedimento de empréstimo de livro já emprestado
- Impedimento de exclusão de autor com livros cadastrados
- Impedimento de exclusão de categoria com livros cadastrados
- Validação de credenciais no login
- Isolamento de dados por usuário autenticado

---

## 🔄 CI/CD

A cada push na branch `main`:

1. **GitHub Actions** executa todos os testes automaticamente
2. **Railway** detecta o push e realiza o deploy em produção automaticamente

O deploy só acontece após os testes passarem, garantindo que código quebrado nunca vá para produção.

---

## 📁 Estrutura do Projeto

```
src/main/java/com/murilo/bookshelf_manager/
├── config/
│   ├── cache/          # Configuração do Caffeine Cache
│   └── security/       # JWT Filter, Security Config, Token Provider
├── controller/         # Endpoints REST
├── dto/                # DTOs de request e response
├── entity/             # Entidades JPA
├── enums/              # Enum de Status do livro
├── exception/          # Exceções customizadas e GlobalExceptionHandler
├── handler/            # GlobalExceptionHandler
├── repository/         # Interfaces Spring Data JPA
└── service/            # Regras de negócio
src/main/resources/
├── db/migration/       # Migrations Flyway (V1 a V6)
└── application.yml
.github/
└── workflows/
    └── pipeline.yaml   # CI/CD Pipeline
```

---

## 👨‍💻 Autor

**Murilo Santos Telles**
Estudante de Engenharia de Software — UniAcademia Centro Universitário

[![LinkedIn](https://img.shields.io/badge/LinkedIn-murilo--santos--telles-blue)](https://www.linkedin.com/in/murilo-santos-telles)
[![GitHub](https://img.shields.io/badge/GitHub-murilosantostelles-black)](https://github.com/murilosantostelles)
