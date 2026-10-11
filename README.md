# Modelo Relacional e Diagrama - Sistema de Gerenciamento de Finanças

## 1. Diagrama de Entidade-Relacionamento (ER)

O diagrama abaixo descreve a estrutura das tabelas, chaves primárias (PK), chaves estrangeiras (FK) e os relacionamentos entre os módulos de usuários, contas, cartões, despesas, financiamentos e investimentos.


---
```mermaid
erDiagram
    USERS ||--o{ BILL : owns
    USERS ||--o{ CREDIT_CARD : owns
    USERS ||--o{ CATEGORY : owns
    USERS ||--o{ FINANCING : owns
    USERS ||--o{ INVESTMENTS : owns

    BILL ||--o{ TRANSACTION : records
    BILL ||--o{ FIXED_BILLS : pays
    BILL ||--o{ FINANCING_INSTALLMENTS : pays

    CREDIT_CARD ||--o{ CREDIT_CARD_STATEMENT : has
    CREDIT_CARD_STATEMENT ||--o{ TRANSACTION : groups

    CATEGORY ||--o{ TRANSACTION : classifies
    CATEGORY ||--o{ FIXED_BILLS : classifies

    FINANCING ||--o{ FINANCING_INSTALLMENTS : generates

    USERS {
        UUID id PK
        String name
        String email UK
        String hashPwd
        LocalDateTime createdAt
    }

    BILL {
        UUID id PK
        String name
        String type
        BigDecimal currentBalance
        String currency
        UUID user FK
    }

    CREDIT_CARD {
        UUID id PK
        String name
        BigDecimal totalLimit
        LocalDate closingDay
        LocalDate dueDay
        UUID user FK
    }

    CREDIT_CARD_STATEMENT {
        UUID id PK
        Integer month
        Integer year
        BigDecimal totalVAlue
        String status
        UUID creditCard FK
    }

    CATEGORY {
        UUID id PK
        String name
        String type
        UUID user FK
    }

    TRANSACTION {
        UUID id PK
        String description
        BigDecimal value
        String type
        LocalDate transactionDate
        UUID bill FK
        UUID creditCardStatement FK
        UUID category FK
    }

    FIXED_BILLS {
        UUID id PK
        String description
        BigDecimal value
        LocalDate dueDay
        String frequency
        boolean Isactive
        UUID bill FK
        UUID category FK
    }

    FINANCING {
        UUID id PK
        String description
        BigDecimal totalValue
        BigDecimal annualInterestRate
        Integer totalInstallments
        LocalDate startDate
        UUID user FK
    }

    FINANCING_INSTALLMENTS {
        UUID id PK
        Integer installmentNumber
        BigDecimal installmentPrice
        LocalDate dueDate
        String status
        UUID financing FK
        UUID bill FK
    }

    INVESTMENTS {
        UUID id PK
        String assetCode UK
        String assetType
        String broker
        BigDecimal quantity
        BigDecimal averagePrice
        BigDecimal currentValue
        LocalDate lastContributionDate
        UUID user FK
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