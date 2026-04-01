![Java CI with Maven](https://github.com/scaglia-aylla1/financeiro/actions/workflows/ci.yml/badge.svg)

# Sistema Financeiro Pessoal

API REST para controle de receitas e despesas, com autenticação JWT, categorias e relatório de balanço mensal.

## Tecnologias

- **Java 21** 
- **Spring Boot 3.5** 
- **Spring Security** 
- **JPA/Hibernate** 
- **PostgreSQL**
- **SpringDoc OpenAPI** (Swagger) 
- **Lombok** 
- **JWT (Auth0)**
- **Docker**

## 🧪 Estratégia de Testes (Qualidade & Segurança)
O projeto segue as melhores práticas de engenharia de software, garantindo que a lógica de negócio e a segurança estejam protegidas contra regressões.

**Testes de Unidade** (Service Layer): Implementados com JUnit 5 e Mockito. Validam regras de negócio complexas e garantem a Prevenção de IDOR (um usuário nunca acessa dados de outro).

**Testes de Integração** (Repository Layer): Utilizam @DataJpaTest e banco H2. Validam queries JPQL customizadas, filtros de data e o comportamento de auditoria JPA.

**Testes de Endpoint** (Controller Layer): Utilizam MockMvc para validar contratos de API, códigos de status HTTP e o funcionamento do GlobalExceptionHandler.

Para rodar os testes:
```
  ./mvnw test
```
## 🏗️ Arquitetura e Diferenciais
Auditoria Automática: Todas as entidades herdam de AbstractAuditEntity, registrando automaticamente data de criação/atualização e o usuário responsável.

Tratamento Global de Erros: Respostas padronizadas para exceções de validação, autenticação e recursos não encontrados.

Padronização DTO: Uso de Java Records para objetos de transferência imutáveis e concisos.

## 🗄️ Gerenciamento de Banco de Dados (Flyway)

Este projeto utiliza o **Flyway** para garantir que a estrutura do banco de dados seja consistente em todos os ambientes (Desenvolvimento, Teste e Produção). 

- **Versionamento:** Todas as alterações no schema (criação de tabelas, novos campos) são feitas via scripts SQL na pasta `src/main/resources/db/migration`.
- **Integridade:** O Hibernate está configurado com `ddl-auto=validate`, garantindo que o código Java esteja sempre em sincronia com as tabelas reais.

**Scripts Atuais:**
- `V1`: Criação do schema inicial (Usuários, Categorias, Receitas).
- `V2`: Adição do campo `observacao` na tabela de receitas para suporte a textos longos.
## 🚀 Como Executar o Projeto

Você tem duas formas de rodar a aplicação: utilizando o Docker (recomendado) ou localmente com Maven.

### 1. Via Docker (Recomendado)
Certifique-se de ter o **Docker** e o **Docker Compose** instalados. Na raiz do projeto, execute:

```bash
docker-compose up --build
```
Este comando irá baixar a imagem do PostgreSQL, compilar o Java, rodar as migrations do Flyway e subir a API em http://localhost:8080 automaticamente.

### 2. Via Maven (Local)
Caso prefira rodar sem Docker, você precisará de um banco PostgreSQL rodando localmente.

1- Configure as variáveis de ambiente no seu application.properties ou no sistema.

2- Instale as dependências e compile:

```Bash
mvn clean install
```
3- Execute a aplicação:

```Bash
mvn spring-boot:run
```
## 🚀 Testando a API:

Importe a coleção disponível em docs/postman/collection.json no seu Postman para testar todos os endpoints rapidamente.

## 🔐 Configuração de Segurança
O projeto utiliza variáveis de ambiente para gerenciar credenciais sensíveis.

*  Renomeie o arquivo .env.example para .env.

* Preencha com suas configurações locais.

* O Spring Boot carregará essas configurações automaticamente via sistema/IDE.

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
