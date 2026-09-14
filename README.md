# 💰 Sistema de Controle Financeiro Pessoal

API REST para gerenciamento de finanças pessoais desenvolvida com **Java 21 e Spring Boot**.

O sistema permite que usuários autenticados gerenciem receitas, despesas e categorias, acompanhem informações financeiras e mantenham seus dados isolados de outros usuários.

O projeto foi desenvolvido como parte do meu portfólio para colocar em prática conceitos de **desenvolvimento backend, segurança, persistência de dados, testes automatizados, migrations, documentação de APIs, Docker e integração contínua**.

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

- PostgreSQL 15
- Flyway

### Segurança

- Spring Security
- JWT
- Access Token e Refresh Token
- Rotação de tokens
- Autenticação e autorização
- Proteção de recursos por usuário

### Testes

- JUnit 5
- Mockito
- MockMvc
- Spring Boot Test
- Testes da camada de persistência

### Documentação e infraestrutura

- Swagger / OpenAPI
- Docker
- Docker Compose
- GitHub Actions

---

## 📌 Funcionalidades

Entre as principais funcionalidades da aplicação estão:

- cadastro e autenticação de usuários;
- geração de Access Token e Refresh Token;
- renovação e rotação de tokens;
- gerenciamento de receitas;
- gerenciamento de despesas;
- gerenciamento de categorias;
- consulta de lançamentos financeiros;
- consulta de lançamentos recentes para o dashboard;
- filtros e paginação;
- cálculo e consulta de informações financeiras;
- validação dos dados recebidos pela API;
- tratamento global de exceções;
- auditoria de entidades;
- isolamento dos dados financeiros por usuário.

---

## 🔐 Autenticação e segurança

A autenticação da aplicação é implementada com **Spring Security e JWT**.

Após realizar o login, o usuário recebe tokens utilizados para acessar os endpoints protegidos da API.

O projeto utiliza **Access Token e Refresh Token**, permitindo a renovação da autenticação sem exigir um novo login enquanto o refresh token permanecer válido.

Além da autenticação, a aplicação verifica a propriedade dos recursos acessados para garantir que cada usuário tenha acesso apenas aos próprios dados.

Essa validação também ajuda a evitar cenários de **IDOR (Insecure Direct Object Reference)**, nos quais um usuário autenticado tenta acessar ou modificar recursos pertencentes a outro usuário através da alteração de identificadores enviados à API.

---

## 🏗️ Organização do backend

A aplicação foi organizada separando as principais responsabilidades em pacotes.

```text
src/main/java/.../
│
├── config/
├── controller/
├── dto/
├── enums/
├── exception/
├── mapper/
├── model/
├── repository/
├── service/
└── FinanceiroApplication.java
```

### Responsabilidades

- **config** — configurações da aplicação, incluindo segurança e CORS;
- **controller** — endpoints REST e tratamento das requisições HTTP;
- **dto** — objetos utilizados na entrada e saída de dados;
- **enums** — valores enumerados utilizados pelo domínio;
- **exception** — exceções e tratamento centralizado de erros;
- **mapper** — conversão entre entidades e DTOs;
- **model** — entidades e modelos da aplicação;
- **repository** — acesso e persistência dos dados;
- **service** — regras de negócio e coordenação das operações.

De forma simplificada, o fluxo principal segue:

```text
Cliente
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL
```

DTOs e mappers são utilizados para separar a representação externa da API das entidades utilizadas na persistência.

---

## 🗄️ Persistência de dados

A persistência da aplicação utiliza:

- PostgreSQL;
- Spring Data JPA;
- Hibernate.

As alterações na estrutura do banco são versionadas com **Flyway**, mantendo as migrations junto ao código da aplicação.

---

## 🧪 Testes automatizados

O projeto possui testes automatizados para validar diferentes comportamentos da aplicação.

São utilizados:

- **JUnit 5** para estruturação dos testes;
- **Mockito** para testes unitários e isolamento de dependências;
- **MockMvc** para testes de endpoints;
- recursos de teste do Spring para validação da integração entre componentes;
- testes da camada de persistência.

Além da execução local, a suíte de testes faz parte da pipeline de integração contínua.

---

## ⚠️ Tratamento de erros

A aplicação possui tratamento centralizado de exceções para manter as respostas de erro da API consistentes.

Entre os cenários tratados estão:

- dados inválidos;
- recursos não encontrados;
- erros de autenticação;
- tentativas de acesso não autorizado;
- violações de regras da aplicação.

Essa abordagem evita duplicação do tratamento de erros nos controllers e mantém um padrão de resposta para os consumidores da API.

---

## 📖 Documentação da API

A API utiliza **Swagger/OpenAPI** para documentação dos endpoints.

Com a aplicação em execução, a interface do Swagger pode ser acessada em:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação permite visualizar os endpoints disponíveis, parâmetros, modelos e respostas da API.

---

## 🐳 Docker

O projeto possui configuração com **Docker Compose** para executar a aplicação e o PostgreSQL em containers.

O ambiente contém dois serviços principais:

```text
Docker Compose
│
├── db
│   └── PostgreSQL 15
│
└── app
    └── Spring Boot / Java 21
```

A aplicação aguarda o banco de dados estar disponível antes de iniciar e possui health check para acompanhamento do estado do serviço.

### Executando com Docker

Com Docker e Docker Compose instalados:

```bash
docker compose up --build
```

A API ficará disponível em:

```text
http://localhost:8080
```

Para encerrar os containers:

```bash
docker compose down
```

---

## ⚙️ Executando localmente

### Pré-requisitos

Para executar o backend fora do Docker:

- Java 21;
- Maven;
- PostgreSQL.

### 1. Clone o repositório

```bash
git clone https://github.com/scaglia-aylla1/financeiro.git
```

### 2. Entre no diretório

```bash
cd financeiro
```

### 3. Configure o ambiente

O repositório possui um arquivo:

```text
.env.example
```

Ele serve como referência para as configurações necessárias.

Não versione senhas, secrets ou credenciais reais no repositório.

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

## 🔄 Integração contínua

O projeto utiliza **GitHub Actions** para validar automaticamente o backend.

A pipeline é executada em:

- `push` para `main` ou `master`;
- `pull request` direcionado para `main` ou `master`.

Durante a execução:

1. o código do repositório é obtido pelo runner;
2. o **JDK 21 (Temurin)** é configurado;
3. o cache do Maven é habilitado;
4. um serviço com **PostgreSQL 15** é iniciado;
5. as configurações necessárias para conexão com o banco são fornecidas ao Spring;
6. o Maven executa:

```bash
mvn -B clean verify
```

Dessa forma, o build e a suíte de testes são executados automaticamente antes da integração das alterações.

O PostgreSQL utilizado no workflow também permite validar a inicialização da aplicação com o banco utilizado pelo projeto, incluindo **Flyway e JPA/Hibernate**.

---

## 🔧 Configuração para produção

O projeto possui um profile específico para produção que utiliza variáveis de ambiente para informações sensíveis e configurações dependentes do ambiente.

Entre elas estão:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
JWT_EXPIRATION_MS
CORS_ALLOWED_ORIGINS
```

Isso permite manter credenciais e secrets fora do código-fonte.

O CORS também pode ser configurado através de variável de ambiente, permitindo definir quais aplicações frontend estão autorizadas a consumir a API.

---

## 📚 Principais aprendizados

Durante o desenvolvimento deste projeto, coloquei em prática conceitos importantes de desenvolvimento backend, incluindo:

- criação de APIs REST com Spring Boot;
- organização de responsabilidades em camadas;
- autenticação e autorização com Spring Security;
- utilização de JWT, Access Token e Refresh Token;
- proteção dos dados pertencentes a diferentes usuários;
- persistência com JPA/Hibernate e PostgreSQL;
- migrations com Flyway;
- DTOs e mapeamento de dados;
- validação de entrada;
- tratamento centralizado de exceções;
- testes automatizados;
- documentação de APIs com Swagger/OpenAPI;
- containerização com Docker;
- execução de múltiplos serviços com Docker Compose;
- automação de build e testes com GitHub Actions;
- configuração de diferentes ambientes através de variáveis de ambiente.

Mais do que implementar as funcionalidades do sistema financeiro, o projeto me permitiu compreender melhor como **segurança, persistência, testes, configuração e infraestrutura se relacionam em uma aplicação backend**.

---

## 👩‍💻 Autora

**Aylla Scaglia**

Desenvolvedora em início de carreira com foco em **Backend Java**, construindo projetos com Java, Spring Boot e tecnologias relacionadas ao desenvolvimento de APIs.

GitHub: `scaglia-aylla1`  
LinkedIn: `aylla-scaglia`
