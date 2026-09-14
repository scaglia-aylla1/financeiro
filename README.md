![Java CI with Maven](https://github.com/scaglia-aylla1/financeiro/actions/workflows/ci.yml/badge.svg)

# 💰 Sistema de Controle Financeiro Pessoal

API REST desenvolvida com **Java e Spring Boot** para gerenciamento de finanças pessoais.

O projeto permite que usuários autenticados gerenciem suas receitas, despesas e categorias, mantendo os dados financeiros isolados por usuário.

Além das funcionalidades da aplicação, o projeto foi desenvolvido como parte do meu portfólio para colocar em prática conceitos de **desenvolvimento backend, segurança, persistência de dados, testes automatizados, migrations, documentação de APIs e containerização**.

---

## 🚀 Tecnologias

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate
- Maven

### Banco de dados

- PostgreSQL
- Flyway
- H2 para testes

### Segurança

- JWT (JSON Web Token)
- Access Token e Refresh Token
- Autenticação e autorização com Spring Security
- Proteção dos recursos por usuário

### Testes

- JUnit 5
- Mockito
- MockMvc
- `@DataJpaTest`

### Documentação e infraestrutura

- Swagger / OpenAPI
- Docker
- GitHub Actions

---

## 📌 Funcionalidades

A API possui funcionalidades para gerenciamento financeiro individual, incluindo:

- cadastro e autenticação de usuários;
- geração de Access Token e Refresh Token;
- gerenciamento de receitas;
- gerenciamento de despesas;
- gerenciamento de categorias;
- consulta de dados financeiros;
- filtros e paginação;
- cálculo e consulta de balanços financeiros;
- validação dos dados de entrada;
- tratamento global de exceções;
- auditoria de entidades;
- isolamento dos dados entre usuários.

---

## 🔐 Segurança

A autenticação da aplicação é baseada em **JWT** utilizando Spring Security.

Após realizar o login, o usuário recebe tokens utilizados para acessar os endpoints protegidos da API.

Além da autenticação, a aplicação aplica autorização sobre os recursos para garantir que cada usuário tenha acesso apenas aos próprios dados.

Essa validação também protege a aplicação contra cenários de **IDOR (Insecure Direct Object Reference)**, nos quais um usuário poderia tentar acessar ou alterar um recurso pertencente a outro usuário apenas modificando seu identificador na requisição.

---

## 🏗️ Arquitetura

O backend foi organizado em camadas, separando as principais responsabilidades da aplicação.

```text
src/main/java
│
├── controller
├── service
├── repository
├── model
├── dto
├── security
├── exception
└── config
```

De forma simplificada, o fluxo de uma requisição segue:

```text
Cliente
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Banco de Dados
```

### Controller

Responsável por receber as requisições HTTP, validar os dados de entrada e retornar as respostas da API.

### Service

Concentra as regras de negócio da aplicação e coordena as operações necessárias para cada caso de uso.

### Repository

Responsável pelo acesso e persistência dos dados utilizando Spring Data JPA.

### DTOs

Utilizados para controlar os dados recebidos e retornados pela API, evitando expor diretamente as entidades de persistência.

---

## 🗄️ Persistência de dados

A persistência é realizada com:

- PostgreSQL;
- Spring Data JPA;
- Hibernate.

As alterações na estrutura do banco de dados são versionadas utilizando **Flyway**, permitindo que a evolução do schema seja controlada junto com o código da aplicação.

---

## 🧪 Testes

O projeto possui testes automatizados em diferentes partes da aplicação.

Foram utilizados:

- **JUnit 5** para estruturação dos testes;
- **Mockito** para testes unitários e isolamento de dependências;
- **H2** para cenários de integração com banco de dados;
- **`@DataJpaTest`** para validação da camada de persistência;
- **MockMvc** para testes dos endpoints da API.

Os testes ajudam a validar regras de negócio, persistência, segurança e comportamento dos endpoints.

---

## ⚠️ Tratamento de erros

A aplicação possui tratamento global de exceções para padronizar as respostas de erro da API.

Entre os cenários tratados estão:

- dados inválidos;
- recursos não encontrados;
- erros de autenticação;
- acesso não autorizado;
- violações de regras da aplicação.

Isso evita que cada controller precise implementar seu próprio tratamento de erro.

---

## 📖 Documentação da API

A API é documentada utilizando **Swagger/OpenAPI**.

Com a aplicação em execução, a documentação interativa pode ser utilizada para visualizar os endpoints, parâmetros, modelos de dados e testar requisições.

```text
http://localhost:8080/swagger-ui/index.html
```

> A URL pode variar de acordo com a configuração utilizada para executar a aplicação.

---

## 🐳 Docker

O projeto possui configuração com Docker para facilitar a execução da aplicação e de suas dependências.

Com Docker instalado, o ambiente pode ser iniciado utilizando:

```bash
docker compose up --build
```

Para encerrar:

```bash
docker compose down
```

---

## ⚙️ Executando localmente

### Pré-requisitos

Para executar o projeto localmente, é necessário ter instalado:

- Java 21;
- Maven;
- PostgreSQL;

ou utilizar Docker para subir as dependências necessárias.

### 1. Clone o repositório

```bash
git clone https://github.com/scaglia-aylla1/financeiro.git
```

### 2. Entre no diretório

```bash
cd financeiro
```

### 3. Configure o banco de dados

Configure as variáveis/propriedades necessárias para conexão com PostgreSQL de acordo com o ambiente utilizado.

Nunca versione senhas, secrets ou chaves JWT diretamente no repositório.

### 4. Execute os testes

Linux/macOS:

```bash
./mvnw test
```

Windows:

```bash
mvnw.cmd test
```

### 5. Execute a aplicação

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

---

## 🔄 CI/CD

O projeto utiliza **GitHub Actions** para automatizar verificações do projeto.

A pipeline permite executar etapas como build e testes automaticamente a partir de alterações enviadas ao repositório.

Isso ajuda a identificar problemas antes que novas alterações sejam incorporadas ao projeto.

---

## 📚 Principais aprendizados

Durante o desenvolvimento deste projeto, pude colocar em prática conceitos importantes de desenvolvimento backend, entre eles:

- estruturação de uma API REST com Spring Boot;
- separação de responsabilidades em camadas;
- autenticação e autorização com Spring Security e JWT;
- proteção de recursos pertencentes a diferentes usuários;
- persistência com JPA/Hibernate;
- migrations de banco de dados com Flyway;
- tratamento centralizado de exceções;
- validação de dados;
- testes unitários e de integração;
- documentação de APIs com OpenAPI;
- utilização de Docker;
- automação de build e testes com GitHub Actions.

Mais do que implementar funcionalidades, o projeto serviu para praticar como diferentes partes de uma aplicação backend se relacionam e como decisões de segurança, persistência e testes afetam a estrutura do sistema.

---

## 👩‍💻 Autora

**Aylla Scaglia**

Desenvolvedora em início de carreira com foco em **Backend Java**, estudando e construindo projetos com Java, Spring Boot e tecnologias relacionadas ao desenvolvimento de APIs.

- GitHub: [scaglia-aylla1](https://github.com/scaglia-aylla1)
- LinkedIn: [Aylla Scaglia](https://www.linkedin.com/in/aylla-scaglia/)
