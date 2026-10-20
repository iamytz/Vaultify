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


## Como Funciona a Geração de Token Via JWT

---

1. Criar uma classe de  serviço para o JWT

```java
package com.carmo.Vaultify.Service;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    //.....
}
```

2. Criar uma String SECRET_KEY que ficara alocada no .env 
```java
package com.carmo.Vaultify.Service;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
   @Value("${SECRET_KEY}")
   private String SECRET_KEY;
}
```
3. Criar um metodo para gerar tokens, ira retornar a string do token
```java
package com.carmo.Vaultify.Service;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
   @Value("${SECRET_KEY}")
   private String SECRET_KEY;

   public String generateToken(String email){
      return Jwts.builder()
              .setSubject(email)
              .setIssuedAt(new Date(System.currentTimeMillis()))   //seta o momento exato da criacao do token
              .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 10)) //expira em 10 horas
              .signWith(getSignInKey(), SignatureAlgorithm.HS256)   //escolhe a KEY de verificação e o algorito usado para gerar o token (SHA256)
              .compact();
   }
}
```
4. Criar um metodo retonar Claims extractAllClaims -> Claims são as informações dentro do token no formato de dicionario
```java
    // metodo para instanciar um objeto que possui todas as claims (delcaracoes do token) ex:dono,quem emitiu, data de expiracao etc..
private Claims extractAllClaims(String token) {
   return Jwts.parserBuilder()
           .setSigningKey(getSignInKey())
           .build()
           .parseClaimsJws(token)
           .getBody();
}
```
5. Criar um metodo aparte que retorna apenas o Claim(declaração) desejada
```java
// o return <T> T indica que pode retornar qualquer tipo de dado
//  basicamente o metodo precisa receber o token e o objeto Claims::parametro
// onde parametro indica oque voce quer retirar do token e utilizar, como -> subject, expiration
public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
   final Claims claims = extractAllClaims(token);
   return claimsResolver.apply(claims);
}
```

6. Criar metodos separados par acada um ter sua função de retirar um dado especifico do token
```java
//primeira instancia de extractClaim onde passamos o token e o objetos que queremos retirar dele.
private Date extractExpiration(String token) {
   return extractClaim(token,Claims::getExpiration);
}


//extrair o subject (email) dentro do token atraves do metodo extractClaim
public String extracEmail(String token) {
   return extractClaim(token, Claims::getSubject);
}
```
7. Criar metodos para facilitar a vida -> metodos que ao ser chamados verificam se o token foi expirado e comparar com o email enviado para validar.
```java
    // metodo para verificar se o token esta expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); //pega a data de expiração e compara com o before(antes) verificando se a data de exipiração é antes
    }

    //valida se o token pertence ao usuario correto, e se nao expirou
    public boolean isTokenValid(String token,String email) {
        final String extractedEmail = extracEmail(email);       // extrai o email do token e compara com o email enviado
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }
```
## Criando Filtro de verificação de token para cada roda acessada
1. Criar JwtAuthenticationFilter.java 
```java

```




```java

```