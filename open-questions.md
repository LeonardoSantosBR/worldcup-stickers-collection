# Lacunas Conhecidas e Decisões Pendentes

O que está **faltando ou indefinido** no domínio. Documentado aqui para que a IA não invente
uma regra ao encontrar o vazio, e para que o comportamento atual não seja confundido com
decisão deliberada.

Nada aqui é um pedido de implementação. É um mapa.

---

## Bloqueantes de produto

### OQ-01 — Falta o outbox _(resolvido)_
✅ `GET /user-trader-offers/inbox` implementado (`TO-28`…`TO-36`) — o receiver já descobre as
ofertas que recebeu, com nome do proposer e das figurinhas.

✅ Falta o lado do proposer: não há como ver as ofertas **enviadas**.
`findByProposerIdAndStatus` continua pronto e sem uso.
→ `GET /user-trade-offers/outbox`, espelhando o inbox.

### OQ-02 — `CANCELLED` é um estado órfão _(resolvido)_
✅ Está no `TradeStatusEnum` e no javadoc de `UserTradeOffersLogsEntity` ("CANCELLED quando o
proposer desiste"), mas nenhuma transição o produz. `findByIdAndProposerId` existe e não é
usado. Ver `TO-02`.
→ `POST /user-trade-offers/{id}/cancel`, restrito ao proposer, só sobre `PENDING`.

### OQ-03 — Não é possível esvaziar a vitrine _(resolvido)_
✅ `MakeAvailableTradeDto.stickerIds` é `@NotEmpty` e a operação substitui a lista inteira
(`TI-03`). Não existe caminho para "não quero trocar mais nada".
→ Permitir lista vazia, ou criar endpoint de remoção.

---

## Game design não definido

### OQ-04 — Pacotes são infinitos e gratuitos _(resolvido)_
✅ Cada pacote custa 7 coins. `POST /stickers/open-package` valida o saldo antes de abrir o
pacote e desconta o custo na mesma transação da abertura. Sem saldo suficiente, a compra é
rejeitada.

### OQ-05 — O álbum não tem recompensa ao completar _(decisão intencional)_
✅ Ao chegar a 100% em `completePercentage`, o usuário não recebe recompensa nem há mudança
de estado. Completar o álbum não tem efeito adicional.

### OQ-06 — Raridade não influencia a troca _(decisão intencional)_
✅ A raridade não impõe regras nem proporções às trocas. Cabe ao usuário avaliar se quer ou não
aceitar uma troca, inclusive quando as figurinhas têm raridades diferentes.

---

## Corretude e robustez

### OQ-07 — Sem controle de concorrência nas trocas
✅ `@Version` em `UserStickerEntity` detecta alterações concorrentes na posse, e `@Version` em
`UserTradeOffersEntity` impede que dois pedidos aceitem ou alterem a mesma oferta com base em
estado desatualizado. As falhas de optimistic locking retornam `409 Conflict`. O aceite mantém
transferências, sincronização de vitrine, invalidação de ofertas e logs dentro da mesma transação.

### OQ-08 — Pool de raridade vazio quebra com 500
`drawStickerByRarity` faz `pool.get(random.nextInt(pool.size()))` sem checar vazio (`PK-08`).
Se o álbum for carregado parcialmente (ex.: nenhuma `LEGENDARY` cadastrada), abrir pacote
estoura `IllegalArgumentException`.

### OQ-09 — Nenhum teste automatizado
`src/test` não cobre nada da regra de negócio. As regras mais frágeis — invalidação em
cascata (`TO-18`), transferência com `quantity = 1` (`CO-04`), sync de vitrine (`TI-07`) —
não têm rede de proteção.
→ As regras deste diretório são a especificação de teste: cada `TO-xx` / `TI-xx` vira um caso.

### OQ-10 — Não há migrations _(resolvido)_
✅ `ddl-auto` do Hibernate gerencia o schema. Sem Flyway/Liquibase, mudanças de schema não são
versionadas nem reproduzíveis, e renomear um valor de enum (`ST-05`) é irreversível.

---

## Segurança

### OQ-11 — Segredos versionados em `application.properties`
Senha do banco em texto e `security.jwt.secret` com valor default. Devem vir de variável de
ambiente. **Se já foram commitados, precisam ser rotacionados** — remover do arquivo não
apaga o histórico do git.

### OQ-12 — `RoleEnum.ADMIN` não autoriza nada
Nenhum endpoint checa role (`AU-10`). O catálogo de figurinhas é somente-leitura por
ausência de endpoint, não por controle de acesso.

### OQ-13 — Sem rate limiting
Nem em `/auth/signin` (força bruta) nem em `/stickers/open-package`.

---

## Qualidade de contrato

### OQ-14 — Duas violações de "entidade não atravessa HTTP" _(resolvido)_
✅ `POST /stickers/open-package` retorna `List<StickerDto>`, sem campos de auditoria da
entidade (`PK-09`). `POST /users` recebe `UserCreateDto` com validação para nome, email e
senha; `role`, `id` e `deletedAt` não fazem parte do contrato de entrada.

### OQ-15 — Três formatos de erro diferentes _(resolvido)_
✅ `GlobalExceptionHandler`, `JwtAuthFilter` e falhas de Bean Validation respondem com
`ErrorResponseDto`: `timestamp`, `status`, `error`, `message` e `fieldErrors`.

### OQ-16 — Endpoints booleanos _(resolvido)_
✅ `POST /users` retorna `201 Created` com `userId`; as operações de publicar/limpar a
vitrine retornam o `TradeInventoryDto` atualizado.

### OQ-17 — `StickersController` acumula dois domínios _(resolvido)_
✅ Os endpoints de vitrine foram movidos para `TradeInventoriesController` sob
`/trade-inventories`; `StickersController` agora depende apenas de `StickersService`.
As rotas antigas sob `/stickers` deixam de existir.

### OQ-18 — `AuthDto` fora do padrão _(resolvido)_
✅ `AuthDto` agora é um `record` com `@NotBlank` nos dois campos e `@Email` no email;
`POST /auth/signin` ativa Bean Validation com `@Valid`.

### OQ-19 — `StickerSummaryDto` pode ser raso demais _(inbox resolvido)_
✅ O inbox devolve `proposerName` e as figurinhas como `{ id, name }` (`TO-33`…`TO-36`),
sem N+1.

❓ `StickerSummaryDto` expõe só `id` e `name`. Uma tela de troca provavelmente quer
`number` (o "#34" do álbum), `rarity` e `imageUrl` — a query em lote já carrega a
`StickerEntity` inteira, então incluí-los não custa nada a mais. Decisão de produto:
esperar a UI pedir, ou já ampliar.
