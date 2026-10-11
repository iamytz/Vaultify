# Modelo Relacional e Diagrama - Sistema de Gerenciamento de Finanças

## 1. Diagrama de Entidade-Relacionamento (ER)

O diagrama abaixo descreve a estrutura das tabelas, chaves primárias (PK), chaves estrangeiras (FK) e os relacionamentos entre os módulos de usuários, contas, cartões, despesas, financiamentos e investimentos.


---

```mermaid
erDiagram
    USUARIOS ||--o{ CONTAS : possui
    USUARIOS ||--o{ CARTOES_CREDITO : possui
    USUARIOS ||--o{ CATEGORIAS : possui
    USUARIOS ||--o{ FINANCIAMENTOS : possui
    USUARIOS ||--o{ INVESTIMENTOS : possui
    
    CONTAS ||--o{ TRANSACOES : registra
    CONTAS ||--o{ CONTAS_FIXAS : debita
    CARTOES_CREDITO ||--o{ FATURAS : possui
    
    FATURAS ||--o{ TRANSACOES : agrupa
    CATEGORIAS ||--o{ TRANSACOES : classifica
    CATEGORIAS ||--o{ CONTAS_FIXAS : classifica
    
    FINANCIAMENTOS ||--o{ PARCELAS_FINANCIAMENTO : gera
    CONTAS ||--o{ PARCELAS_FINANCIAMENTO : debita

    USUARIOS {
        uuid id PK
        string nome
        string email
        string senha_hash
        datetime criado_em
    }

    CONTAS {
        uuid id PK
        uuid usuario_id FK
        string nome
        string tipo
        decimal saldo_atual
        string moeda
    }

    CARTOES_CREDITO {
        uuid id PK
        uuid usuario_id FK
        string nome
        decimal limite_total
        integer dia_fechamento
        integer dia_vencimento
    }

    FATURAS {
        uuid id PK
        uuid cartao_id FK
        integer mes
        integer ano
        decimal valor_total
        string status
    }

    CATEGORIAS {
        uuid id PK
        uuid usuario_id FK
        string nome
        string tipo
    }

    TRANSACOES {
        uuid id PK
        uuid conta_id FK
        uuid cartao_fatura_id FK
        uuid categoria_id FK
        string descricao
        decimal valor
        string tipo
        date data_transacao
    }

    CONTAS_FIXAS {
        uuid id PK
        uuid conta_id FK
        uuid categoria_id FK
        string descricao
        decimal valor
        integer dia_vencimento
        string frequencia
        boolean ativo
    }

    FINANCIAMENTOS {
        uuid id PK
        uuid usuario_id FK
        string descricao
        decimal valor_total
        decimal taxa_juros_anual
        integer total_parcelas
        date data_inicio
    }

    PARCELAS_FINANCIAMENTO {
        uuid id PK
        uuid financiamento_id FK
        uuid conta_id FK
        integer numero_parcela
        decimal valor_parcela
        date data_vencimento
        string status
    }

    INVESTIMENTOS {
        uuid id PK
        uuid usuario_id FK
        string codigo_ativo
        string tipo_ativo
        string instituicao
        decimal quantidade
        decimal valor_medio_compra
        decimal valor_atual
        date data_ultimo_aporte
    }
```

---

## 2. Principais Decisões Arquiteturais

1. **Separação de Gastos:**
    - **Transações:** Atende aos gastos variáveis/aleatórios. Aceita débito direto em conta ou vínculo com a fatura do cartão de crédito através de uma restrição `CHECK`.
    - **Contas Fixas:** Tabela separada para servir como modelo/agendador do que precisa ser cobrado periodicamente.
    - **Financiamentos:** Tabela de financiamento desacoplada que gera N registros em `parcelas_financiamento`, permitindo liquidação individual.
2. **Módulo Isolado de Investimentos:**
    - A tabela `investimentos` foi desenhada para guardar posição e saldo sem misturar com transações do dia a dia, facilitando operações de rentabilidade e integração com APIs externas de cotações.
3. **Tipos e Precisão Monetária:**
    - Todos os valores utilizam `DECIMAL(15, 2)` para evitar problemas de arredondamento inerentes ao tipo `FLOAT`.
    - A quantidade de ativos em investimentos usa `DECIMAL(18, 8)` para permitir frações de criptomoedas ou cotas fracionadas.