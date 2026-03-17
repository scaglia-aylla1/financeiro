# Melhorias e próximos passos – Portfólio Júnior

Sugestões para valorizar o projeto na busca do primeiro emprego em tecnologia. Priorize o que for mais viável para você.

---

## Prioridade alta (impacto em processos seletivos)

### 1. Testes automatizados

**Por quê:** Quase toda vaga pede “conhecimento em testes” ou “TDD”. Um projeto com testes mostra que você se importa com qualidade.

**O que fazer:**
- **Testes unitários** dos services (com Mockito): ex.: `CategoriaService.criarCategoria`, `AuthService.login`, `ReceitaService.buscarPorId` (incluindo quando não é dono do recurso).
- **Testes de integração** de 1–2 controllers (com `@SpringBootTest` + `MockMvc`): ex. login retorna 200 e token; listar receitas sem token retorna 401.

**Dica:** Comece por `AuthService` e `CategoriaService` (menos dependências). No README, adicione: “Testes: `mvn test`”.

---

### 2. README profissional (já criado)

**O que já tem:** README com tecnologias, como rodar, endpoints e estrutura.

**Pode melhorar:**
- Adicionar 1–2 screenshots do Swagger ou do resultado de um endpoint.
- Seção “Funcionalidades” em tópicos (ex.: “Filtro de receitas por data e categoria”, “Balanço mensal por usuário”).
- Link para o repositório no topo (quando subir no GitHub).

---

### 3. Migrations de banco (Flyway ou Liquibase)

**Por quê:** Em produção não se usa `ddl-auto=update`. Migrations mostram que você pensa em evolução controlada do banco.

**O que fazer:**
- Adicionar **Flyway** no `pom.xml`.
- Criar `src/main/resources/db/migration/V1__criar_tabelas_iniciais.sql` com o script das tabelas (pode gerar a partir do schema atual do Hibernate ou exportar do PostgreSQL).
- No `application.properties`: `spring.jpa.hibernate.ddl-auto=validate` (ou `none`) e deixar o Flyway criar/atualizar.

---

## Prioridade média (diferenciais)

### 4. Docker

**Por quê:** Muitas vagas pedem “Docker” ou “conteinerização”. Quem for testar seu projeto roda com um comando.

**O que fazer:**
- `Dockerfile` para build da aplicação (multi-stage: Maven build + imagem com JAR).
- `docker-compose.yml` com: app + PostgreSQL (e opcionalmente só Postgres para dev local).
- No README: “Com Docker: `docker-compose up -d`” (e porta do app).

---

### 5. CI/CD básico (GitHub Actions)

**Por quê:** Mostra que você conhece pipeline (build + testes automáticos).

**O que fazer:**
- Workflow que rode em todo push/PR: `mvn clean test` (e opcionalmente `mvn compile`).
- Se usar Docker: job que builda a imagem (opcional).

Arquivo: `.github/workflows/ci.yml`.

---

### 6. Variáveis de ambiente e segurança

**Por quê:** Mostra preocupação com segurança e ambientes (dev vs prod).

**O que já tem:** Uso de `${SPRING_DATASOURCE_URL:default}`, etc.

**Pode melhorar:**
- README ou `.env.example` listando: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `API_SECURITY_TOKEN_SECRET`.
- Nunca commitar senhas; em exemplos usar placeholders.

---

## Prioridade opcional (se der tempo)

### 7. Padrão de resposta da API

- Envelope padrão para sucesso: `{ "data": { ... }, "message": "..." }`.
- Manter `ErrorDetails` para erro (já existe); garantir que todos os endpoints usem o mesmo formato.

### 8. Refresh token (JWT)

- Além do token de acesso, retornar um refresh token com validade maior; endpoint `POST /auth/refresh` para renovar o acesso. Mostra que você pensa em experiência e segurança.

### 9. Documentação de decisões

- Arquivo `docs/DECISOES.md` (ou “ADR”) com 2–3 decisões: ex. “Usamos JWT stateless para não depender de sessão no servidor”; “DTOs separados para request e response para evoluir a API sem quebrar cliente”. Bom para entrevistas.

### 10. Postman ou Insomnia

- Coleção com os principais endpoints (auth, criar receita, listar despesas, balanço). Exportar e colocar na pasta `docs/` ou link no README. Facilita para quem for testar.

---

## Ordem sugerida para você

1. **README** – já feito; só revisar e adicionar 1 screenshot se quiser.
2. **Testes** – pelo menos 3–5 testes unitários (services) e 1–2 de integração (auth + um CRUD).
3. **Flyway** – um `V1__*.sql` e desligar `ddl-auto=update`.
4. **Docker** – `Dockerfile` + `docker-compose` com app e PostgreSQL.
5. **GitHub Actions** – `mvn test` em todo push.

Com isso, o projeto já demonstra: código organizado (SOLID), API documentada (Swagger), testes, preocupação com banco (migrations), containerização e pipeline. É um portfólio forte para posição júnior.

Se quiser, posso te guiar na implementação de um desses itens (por exemplo: primeiros testes ou Flyway) passo a passo no código.
