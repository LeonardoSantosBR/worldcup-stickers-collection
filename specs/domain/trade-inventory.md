# Trade Inventory — A Vitrine

Prefixo de regra: `TI`

Código: [UserTradeInventoriesService.java](../../src/main/java/com/leonardo/worldcup_stickers/services/UserTradeInventoriesService.java),
[UserTradeInventoriesRepository.java](../../src/main/java/com/leonardo/worldcup_stickers/repositories/UserTradeInventoriesRepository.java)

## Conceito

A vitrine é a **declaração pública** de quais figurinhas o usuário aceita trocar. É o que
torna as figurinhas dele visíveis e "ofertáveis" para os outros.

Vitrine **não** é posse. É intenção. As duas podem divergir temporariamente, e a
reconciliação é explícita (`TI-06`).

## Modelo

`UserTradeInventoryEntity` = tabela `user_trade_inventories`.

| Campo | Regra |
|---|---|
| `user` | `@OneToOne`, **unique** — exatamente uma vitrine por usuário |
| `availableStickerIds` | `bigint[]` do PostgreSQL (`@JdbcTypeCode(SqlTypes.ARRAY)`), default `[]` |

**TI-01 — A vitrine é um array de IDs, não uma tabela de associação.** Escolha deliberada:
a lista inteira é substituída de uma vez, e não há metadado por item. Consultá-la exige
`unnest` em SQL nativo.

## Regras

**TI-02 — Um usuário, uma vitrine.** Criada no cadastro (`AU-03`). `loadOrCreateInventory`
existe como rede de segurança para usuários anteriores a essa regra.

**TI-03 — `POST /stickers/make-available-trade` substitui a vitrine inteira.** Não é
"adicionar". A lista enviada **vira** a vitrine; o que não estiver nela sai. Duplicatas são
colapsadas (`TI-05`).

**TI-04 — `PATCH /stickers/clear-available-trades` esvazia a vitrine do usuário autenticado.**
Não recebe corpo: o usuário é identificado pelo JWT. A operação substitui
`availableStickerIds` por uma lista vazia, atualiza `updatedAt` para o momento da operação
e persiste a entidade. Se a vitrine ainda não existir, ela é criada vazia. Retorna `true`
em caso de sucesso.

**TI-05 — Só se pode disponibilizar o que se possui.** Todo ID enviado precisa existir na
coleção do usuário. Qualquer ID não possuído → `StickersNotOwnedException` → **400**, e
a lista dos IDs inválidos vai na mensagem. Validação é tudo-ou-nada.

**TI-06 — Duplicatas na requisição são colapsadas.** `new LinkedHashSet<>(stickerIds)`
remove repetições preservando a ordem de envio.

**TI-07 — Disponibilizar não reserva nem bloqueia.** A figurinha continua na coleção,
continua contando no progresso do álbum, e pode ser oferecida em quantas ofertas
simultâneas o usuário quiser. Não há lock. Conflitos são resolvidos no momento do aceite
(ver `TO-11` e `TO-14` em [trade-offers.md](trade-offers.md)).

**TI-08 — A vitrine é reconciliada automaticamente após cada troca aceita.**
`syncTradeInventory(userId)` remove da vitrine todo ID que o usuário deixou de possuir.
Roda para **ambos** os lados da troca. Só grava se algo mudou.

> Isso é o único mecanismo de limpeza. Nenhum outro fluxo remove item da vitrine.

**TI-09 — Consultar figurinhas quantidade-1 na vitrine é permitido.** Colocar na vitrine a
única cópia que se tem é válido; o sistema não impede negociar uma figurinha não-repetida.

## Vitrine pública

**TI-10 — `GET /stickers/available-trades` lista a vitrine de todos os outros usuários.**
Query nativa com `CROSS JOIN LATERAL unnest(i.available_sticker_ids)`: cada figurinha
disponível de cada usuário vira uma linha.

**TI-11 — O usuário nunca vê a própria vitrine nesse endpoint.** `WHERE i.user_id <> :userId`.
Para ver a própria, use `GET /users/my-stickers-available-trade` (`CO-10`).

**TI-12 — Filtro opcional por nome do jogador.** Query param `name`, case-insensitive,
substring (`ILIKE '%nome%'` sobre `s.player_name`). `null` ou string em branco → sem
filtro (o service normaliza com `trim()` antes de passar adiante).

**TI-13 — Soft delete é respeitado manualmente na query nativa.** Os `JOIN`s carregam
`AND u.deleted_at IS NULL` e `AND s.deleted_at IS NULL` explicitamente, porque
`@SQLRestriction` **não** se aplica a SQL nativo. Toda query nativa nova precisa repetir isso.

**TI-14 — Ordenação: `s.number ASC, u.name ASC`.**

**TI-15 — Aliases camelCase precisam de aspas duplas.** O PostgreSQL rebaixa identificadores
não-aspeados para minúsculas, o que quebra a projeção de interface do Spring
(`AvailableTradeStickerView`). Por isso `s.id AS "stickerId"`, `u.name AS "ownerName"`, etc.
Regra obrigatória para qualquer projeção nativa nova.

**TI-16 — A resposta identifica o dono.** `AvailableTradeStickerDto` traz `stickerId`,
`stickerName`, `rarity`, `position` + `ownerId`, `ownerName`, `ownerEmail`. O `ownerId` é
o que vai em `receiverId` ao criar uma oferta.
