# Tech Challenger — Fase 03 — Documentação da Aplicação

![microservices](https://img.shields.io/badge/architecture-microservices-blue)
![java](https://img.shields.io/badge/tech-Java%20%7C%20Spring%20Boot-orange)
![kafka](https://img.shields.io/badge/messaging-Kafka-red)

> Uma arquitetura de microserviços demonstrando autenticação com JWT, mensageria com Kafka e integração com um serviço externo de pagamento.

Índice
- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Como subir o ambiente](#como-subir-o-ambiente)
- [Serviços e portas](#serviços-e-portas)
- [Rotas HTTP principais](#rotas-http-principais)
- [Tópicos Kafka](#tópicos-kafka)
- [Exemplos de chamadas (cURL)](#exemplos-de-chamadas-curl)
---

Visão Geral
----------
Este repositório do Tech Challenger da fase 3 da FIAP contém uma arquitetura de microserviços para autenticação, pedidos e pagamentos.
O objetivo é demonstrar separação de responsabilidades, comunicação assíncrona com Kafka e integração com um serviço externo de pagamento.

Arquitetura
----------
- auth-service (porta 8081)
  - Autenticação e cadastro de usuários. Emite JWT usado pelos demais serviços.
- pedido-service (porta 8082)
  - Gerencia pedidos. Exige JWT válido e publica eventos em Kafka.
- pagamento-service (porta 8083)
  - Processa pagamentos de forma assíncrona. Não expõe rotas HTTP públicas.
- procpag (porta 8089)
  - Serviço externo de processamento de pagamentos (imagem Docker). OpenAPI em `http://localhost:8089/openapi.yml`.
- postgres (porta 5432)
  - Banco compartilhado com bancos lógicos separados (authdb, pedidodb, pagamentodb).
- kafka (porta 9092) + kafka-ui (porta 8085)
  - Mensageria para eventos de pedido e pagamento.

Padrão de camadas (por serviço)
1. application — casos de uso e DTOs
2. domain — entidades e regras de negócio
3. infrastructure — controllers HTTP, persistência, mensageria, mappers e configs

Como subir o ambiente
--------------------
Recomendado: docker e docker-compose instalados.

No diretório raiz do projeto:

```bash
docker compose up -d --build
```

Isso irá construir e subir todos os serviços listados em segundos.

Serviços e portas
-----------------
- auth-service -> http://localhost:8081
- pedido-service -> http://localhost:8082
- pagamento-service -> http://localhost:8083
- procpag -> http://localhost:8089
- kafka-ui -> http://localhost:8085

Rotas HTTP principais
---------------------
auth-service (8081)
- POST /auth/register — Cria um usuário.
- POST /auth/login — Autentica e retorna JWT.

pedido-service (8082) — todas as rotas exigem header `Authorization: Bearer <token>`
- POST /orders — Cria um pedido.
- GET /orders — Lista pedidos do usuário autenticado.
- GET /orders/{id} — Busca pedido por id (apenas do usuário proprietário).
- POST /orders/{id}/confirm — Confirma pedido.

pagamento-service (8083)
- Não possui endpoints HTTP públicos. Processamento ocorre via Kafka.

Tópicos Kafka
-------------
- pedido.criado — Publicado pelo `pedido-service`. Consumido pelo `pagamento-service`.
- pagamento.aprovado — Publicado pelo `pagamento-service`. Consumido pelo `pedido-service`.
- pagamento.pendente — Publicado pelo `pagamento-service`. Consumido pelo `pedido-service`.

Exemplos de chamadas (cURL)
---------------------------
1) Registrar usuário

```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Joao Silva",
    "email": "joao@exemplo.com",
    "password": "senha123"
  }'
```

2) Login

```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@exemplo.com",
    "password": "senha123"
  }'
```
Resposta esperada (exemplo):

```json
{
  "token": "eyJhbGciOi...",
  "type": "Bearer",
  "userId": 1,
  "email": "joao@exemplo.com",
  "role": "CUSTOMER"
}
```

3) Criar pedido

```bash
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_AQUI>" \
  -d '{
    "restaurant": {
      "restaurantId": 10,
      "restaurantName": "Restaurante Central"
    },
    "items": [
      { "productId": 1, "name": "Hamburguer", "quantity": 2, "price": 25.90 },
      { "productId": 2, "name": "Refrigerante", "quantity": 1, "price": 6.50 }
    ]
  }'
```

4) Listar meus pedidos

```bash
curl -X GET http://localhost:8082/orders \
  -H "Authorization: Bearer <TOKEN_AQUI>"
```

5) Buscar pedido por id

```bash
curl -X GET http://localhost:8082/orders/1 \
  -H "Authorization: Bearer <TOKEN_AQUI>"
```

6) Confirmar pedido

```bash
curl -X POST http://localhost:8082/orders/1/confirm \
  -H "Authorization: Bearer <TOKEN_AQUI>"
```

Observações técnicas
-------------------
- JWT é obrigatório no `pedido-service` (todas as rotas, exceto Swagger).
- O `pagamento-service` integra com o `procpag` via HTTP e possui resiliência com Resilience4j.
- `kafka-ui` facilita inspeção de tópicos e mensagens.

Arquitetura detalhada
---------------------

Diagrama de sequência — fluxo de criação de pedido e pagamento
-------------------------------------------------------------
1) Cliente (UI) -> auth-service: login/obter JWT
2) Cliente -> pedido-service: POST /orders (Authorization: Bearer <JWT>)
3) pedido-service: valida token -> persiste pedido (status=PENDING) -> publica evento `pedido.criado` em Kafka
4) pagamento-service (consumer) <- Kafka: consome `pedido.criado`, tenta processar pagamento via `procpag` (HTTP)
5) procpag -> pagamento-service: responde (APROVADO / PENDENTE / REJEITADO)
6) pagamento-service publica `pagamento.aprovado` ou `pagamento.pendente` em Kafka
7) pedido-service consome resposta e atualiza status do pedido (CONFIRMED / PAYMENT_PENDING / REJECTED)
8) Cliente consulta GET /orders/{id} para verificar status atualizado

Fluxo principal (descrição passo a passo)
-----------------------------------------
- Autenticação
  1. Usuário se cadastra ou realiza login no `auth-service`.
  2. `auth-service` devolve um JWT com claims básicos (userId, role).

- Criação de pedido
  1. Cliente envia POST /orders ao `pedido-service` com JWT no header.
  2. `pedido-service` valida JWT (via chave local/jwks) e autoriza a ação.
  3. Pedido é persistido localmente com status inicial (PENDING).
  4. `pedido-service` publica evento `pedido.criado` no Kafka com payload contendo pedidoId, userId, itens e total.

- Processamento de pagamento (assíncrono)
  1. `pagamento-service` consome `pedido.criado`.
  2. Tenta chamar `procpag` (HTTP) para efetivar o pagamento.
  3. Com base na resposta, publica evento `pagamento.aprovado` ou `pagamento.pendente`.
  4. `pedido-service` consome o resultado e atualiza o status do pedido.

Pontos de resiliência
--------------------------------------------------
Abaixo estão os pontos críticos de falha e as estratégias implementadas/recomendadas para resilência.

1) Comunicação com Kafka
- Possíveis falhas: broker indisponível, latência, mensagens duplicadas ou ordem parcial.
- Estratégias:
  - Retries com backoff exponencial no produtor/consumer.
  - Acknowledgements manuais e commit por lote controlado (consumer).
  - Uso de tópicos de retry e DLQ (dead-letter) para mensagens que falham repetidamente.
  - Idempotência na aplicação (usar orderId/pedidoId como key para evitar duplicações no processamento).
  - Monitoramento de lag de consumer (alertas para offsets).

2) Chamadas HTTP ao `procpag` (serviço externo)
- Possíveis falhas: timeout, erro 5xx, latência, indisponibilidade.
- Estratégias:
  - Circuit Breaker (Resilience4j) para abrir circuito após 5 falhas e evitar sobrecarregar o serviço externo.
  - Retries limitados com jitter e timeout curto por tentativa.
  - Fallbacks: marcar pagamento como PENDING e publicar evento apropriado para reprocessamento manual/assincrono.
  - Dead-letter para requisições que excederem tentativas e alertar operadores.

3) Persistência local (Postgres) — consistência e disponibilidade
- Possíveis falhas: conexão de banco, deadlocks, inconsistência entre serviços.
- Estratégias:
  - Transações locais ao criar pedido e ao publicar evento (outbox pattern recomendado).
  - Pool de conexões configurado com timeouts e reconexão automática.
  - Backups e readiness/liveness probes para evitar nós degradados.

4) Autenticação/Autorização (JWT)
- Possíveis falhas: token expirado, chave de assinatura rotacionada, validação lenta.
- Estratégias:
  - Validação eficiente.
  - Tratamento claro de token expirado (403 com mensagem que instrui a renovação de token).
  - Rate limiting e proteção contra brute-force no `auth-service`.

5) Consumidores Kafka e ordenação/duplicidade
- Possíveis falhas: processamento duplicado, out-of-order.
- Estratégias:
  - Processamento idempotente no consumidor (verificar se evento já aplicado pelo pedidoId).
  - Uso de chave de particionamento consistente para garantir ordering relativo por pedidoId quando necessário.
  - Pause/resume no consumidor em caso de back-pressure.

6) Observabilidade e alertas
- Métricas essenciais: latência das APIs, taxa de erros 4xx/5xx, tempo de resposta do `procpag`, lag dos consumidores Kafka, estado do circuit breaker.
- Logs estruturados (correlationId por fluxo) e tracing distribuído.