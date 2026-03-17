# Sistema Financeiro Pessoal

API REST para controle de receitas e despesas, com autenticação JWT, categorias e relatório de balanço mensal.

## Tecnologias

- **Java 21** · **Spring Boot 3.5** · **Spring Security** · **JPA/Hibernate** · **PostgreSQL**
- **SpringDoc OpenAPI** (Swagger) · **Lombok** · **JWT (Auth0)**

## Como rodar

### Pré-requisitos

- JDK 21
- Maven
- PostgreSQL (ou altere `application.properties` para H2/MySQL)

### Passos

1. Crie o banco no PostgreSQL:
   ```sql
   CREATE DATABASE financeiro;
   ```

2. Ajuste usuário/senha em `src/main/resources/application.properties` se necessário (padrão: `postgres` / `123456`).

3. Execute:
   ```bash
   mvn spring-boot:run
   ```

4. Acesse:
   - **API**: http://localhost:8080  
   - **Swagger**: http://localhost:8080/swagger-ui.html  

### Autenticação

- **Registro**: `POST /api/v1/auth/register` com `name`, `email`, `password`
- **Login**: `POST /api/v1/auth/login` com `email`, `password`
- Use o `token` retornado no header: `Authorization: Bearer <token>`

## Endpoints principais

| Recurso     | Descrição                    |
|------------|------------------------------|
| `/auth`    | Registro e login             |
| `/categorias` | CRUD de categorias (Receita/Despesa) |
| `/receitas`   | CRUD de receitas (filtros e paginação) |
| `/despesas`   | CRUD de despesas (filtros e paginação) |
| `/relatorios/balanco/{ano}/{mes}` | Balanço mensal (receitas - despesas) |

## Estrutura do projeto

```
src/main/java/com/scaglia/financeiro/
├── config/          # Security, JWT, OpenAPI
├── controller/      # REST endpoints
├── dto/             # Request/Response DTOs
├── exception/       # GlobalExceptionHandler, exceções
├── mapper/          # Entity ↔ DTO
├── model/           # Entidades JPA
├── repository/      # Spring Data JPA
└── service/         # Regras de negócio
```

## Licença

Projeto de portfólio — uso livre para estudo.
