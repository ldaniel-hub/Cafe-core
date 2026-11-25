# GraosAPI

API REST em Java 21 e Spring Boot para cadastro de produtores de café, controle de lotes e movimentações de estoque. Utiliza Spring Data JPA, banco H2, validação Jakarta, JWT e testes unitários com JUnit 5 e Mockito.

## Executar

```bash
mvn clean verify
mvn spring-boot:run
```

O banco H2 em memória é recriado ao iniciar a aplicação. A chave JWT de exemplo serve apenas para fins didáticos; em produção, configure `APP_JWT_SECRET` com um segredo seguro.

## Autenticação

Faça `POST /api/auth/login` com JSON `{"username":"admin","password":"Admin@123"}` ou `{"username":"aluno","password":"Aluno@123"}` e envie o token retornado como `Authorization: Bearer <token>`.

- `admin`: leitura e escrita de produtores e lotes.
- `aluno`: somente leitura.

As credenciais e a chave padrão são exemplos didáticos e não devem ser usadas em produção.

## Endpoints

| Método | Endpoint | Ação |
|---|---|---|
| GET | `/api/produtores` | Listar produtores |
| GET | `/api/produtores/{id}` | Consultar produtor |
| POST | `/api/produtores` | Cadastrar produtor |
| PUT | `/api/produtores/{id}` | Atualizar produtor |
| DELETE | `/api/produtores/{id}` | Remover produtor sem lotes |
| GET | `/api/lotes` | Listar lotes |
| GET | `/api/lotes/{id}` | Consultar lote |
| POST | `/api/lotes` | Cadastrar lote (estoque inicial zero) |
| PUT | `/api/lotes/{id}` | Atualizar dados do lote |
| POST | `/api/lotes/{id}/movimentacoes` | Registrar entrada ou saída |
| DELETE | `/api/lotes/{id}` | Remover lote com estoque zero |

O cadastro de produtor recebe `nome`, `email` e `municipio`. O cadastro de lote recebe `codigo`, `variedade` e `produtorId`. Uma movimentação recebe `tipo` (`ENTRADA` ou `SAIDA`) e `quantidadeKg` positiva. Saídas não podem exceder o saldo do lote.

Respostas de criação usam `201 Created`, exclusões usam `204 No Content`, recursos ausentes retornam `404`, dados inválidos retornam `400`, conflitos de integridade retornam `409` e acesso não autorizado retorna `401` ou `403`.

## Integração contínua

O workflow em `.github/workflows/ci.yml` executa `mvn --batch-mode clean verify` a cada push e pull request.
