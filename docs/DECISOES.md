# Registro de Decisões de Arquitetura (ADR)

Este documento registra as decisões técnicas fundamentais tomadas durante o desenvolvimento do projeto **Financeiro**, detalhando o contexto, as escolhas de design e suas justificativas.

---

## 1. Autenticação Stateless com JWT e Refresh Token
**Contexto:** Necessidade de um sistema de autenticação seguro, escalável e que evitasse o armazenamento de sessões no servidor.

**Decisão:** Implementação de **JSON Web Tokens (JWT)** com estratégia de **Refresh Token**.
- **Access Token:** Validade curta (2 horas) para mitigar riscos de interceptação.
- **Refresh Token:** Validade longa (7 dias) para renovação de acesso sem reautenticação manual.

**Justificativa:** Mantém a API *stateless*, facilitando a escalabilidade horizontal e garantindo uma experiência de usuário (UX) fluida.

---

## 2. Uso Estratégico de Java Records
**Contexto:** Necessidade de objetos imutáveis para transporte de dados, especialmente no fluxo de segurança.

**Decisão:** Uso de **Java Records** para os DTOs de Autenticação (`LoginRequestDto`, `RegisterRequestDto`, `RefreshTokenRequestDto` e `ResponseDto`).

**Justificativa:** Garante a imutabilidade dos dados de credenciais e tokens, elimina código repetitivo (*boilerplate*) e provê uma semântica clara de que esses objetos são meros carregadores de dados (*Data Carriers*).

---

## 3. Padronização de Respostas com Envelope `ApiResponse`
**Contexto:** O front-end necessita de uma estrutura previsível para tratar sucessos e falhas de forma genérica.

**Decisão:** Encapsulamento de todas as respostas de sucesso no objeto `ApiResponse<T>`, contendo os campos `data` (payload) e `message` (feedback textual).

**Justificativa:** Permite que o front-end utilize interceptores globais para exibir notificações (toasts) baseadas na mensagem da API, elevando o padrão profissional da comunicação cliente-servidor.

---

## 4. Gerenciamento de Banco de Dados com Flyway
**Contexto:** Evolução do schema do banco de dados de forma controlada e versionada.

**Decisão:** Utilização do **Flyway** para gerenciar migrations de banco de dados.

**Justificativa:** Assegura que todos os ambientes (desenvolvimento, CI/CD e produção) possuam a mesma estrutura de tabelas, eliminando conflitos manuais de schema.

---

## 5. Conteinerização com Docker e Docker Compose
**Contexto:** Necessidade de garantir que a aplicação rode identicamente em qualquer máquina.

**Decisão:** Criação de um `Dockerfile` multi-stage para a aplicação e um `docker-compose.yml` para orquestrar o App e o banco de dados PostgreSQL.

**Justificativa:** Facilita o *onboarding* de novos desenvolvedores (comando único para subir o ambiente) e isola as dependências da aplicação do sistema operacional host.

---

## 6. Estratégia de Testes Automatizados
**Contexto:** Garantir a integridade das regras de negócio e a estabilidade dos contratos da API.

**Decisão:** Implementação de testes unitários e de integração utilizando **JUnit 5**, **Mockito** e **MockMvc**, com perfil de isolamento (`@ActiveProfiles("test")`).

**Justificativa:** Permite refatorações seguras (como a mudança para o envelope `ApiResponse`) com a confiança de que o comportamento funcional permanece intacto.

---

## 7. Pipeline de Integração Contínua (CI)
**Contexto:** Evitar que código quebrado ou com testes falhando seja integrado à branch principal.

**Decisão:** Configuração de **GitHub Actions** para executar automaticamente o build e os testes (`mvn test`) em cada *Push* ou *Pull Request*.

**Justificativa:** Fornece feedback imediato sobre a qualidade do código e garante que a branch `main` esteja sempre em um estado estável e pronto para deploy.
