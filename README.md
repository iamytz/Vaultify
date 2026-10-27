<div align="center">

# 🔐 Vaultify

**Gestão financeira pessoal em uma única aplicação web.**
Contas, cartões, dívidas, financiamentos e investimentos — tudo centralizado, seguro e expansível.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow?style=for-the-badge)

</div>

---

## 📑 Sumário

- [Sobre o projeto](#-sobre-o-projeto)
- [O que já foi implementado](#-o-que-já-foi-implementado)
- [Tecnologias](#-tecnologias)
- [Arquitetura e estrutura](#-arquitetura-e-estrutura)
- [Modelo de dados](#-modelo-de-dados)
- [Decisões de projeto](#-decisões-de-projeto)
- [Endpoints](#-endpoints)
- [Como executar](#-como-executar)
- [Próximos passos](#-próximos-passos)
- [Autor](#-autor)

---

## 💡 Sobre o projeto

A **Vaultify** nasceu de uma necessidade real: controlar gastos em uma planilha, acompanhar investimentos em outro site, anotar dívidas em outro lugar — e, no fim, ter tudo espalhado e o saldo no vermelho.

A proposta é reunir **finanças, dívidas, parcelas e investimentos** em um único sistema, construído para ser **seguro, confiável e expansível**.

O projeto está em **desenvolvimento ativo** e evolui a cada novo desafio, servindo também como laboratório para colocar em prática o que venho estudando continuamente.

---

## ✅ O que já foi implementado

| Módulo | Descrição | Status |
|---|---|---|
| **Modelagem de dados** | 10 entidades JPA mapeadas com relacionamentos, chaves UUID e carregamento *lazy* | ✅ Concluído |
| **Cadastro de usuário** | Criação de conta com validação de dados e persistência no MySQL | ✅ Concluído |
| **Segurança de senha** | Política de senha forte + hash com **BCrypt** antes de salvar | ✅ Concluído |
| **Página de login** | Tela renderizada pelo servidor (Thymeleaf) com HTML, CSS e JavaScript | ✅ Concluído |
| **Autenticação** | Endpoint de login e integração com a tela | 🚧 Em evolução |

### Política de senha

Ao cadastrar um usuário, a senha precisa conter:

- mínimo de **8 caracteres**
- ao menos **1 letra maiúscula** e **1 minúscula**
- ao menos **1 número**
- ao menos **1 caractere especial**

Senhas fora do padrão são rejeitadas com `400 Bad Request`. As aceitas são armazenadas apenas como **hash BCrypt** — a senha original nunca é persistida.

---

## 🛠 Tecnologias

| Camada | Tecnologia |
|---|---|
| **Linguagem** | Java 21 |
| **Framework** | Spring Boot 4.1.1 (Spring Web MVC) |
| **Persistência** | Spring Data JPA / Hibernate |
| **Banco de dados** | MySQL |
| **Segurança** | Spring Security (`BCryptPasswordEncoder`) |
| **Front-end** | Thymeleaf, HTML5, CSS3, JavaScript |
| **Documentação de API** | springdoc-openapi |
| **Produtividade** | Lombok, Spring Boot DevTools |
| **Build** | Maven (Maven Wrapper incluso) |

---

## 🧱 Arquitetura e estrutura

O projeto segue uma organização em camadas, separando responsabilidades de apresentação, regra de negócio e acesso a dados.

```text
Vaultify
├── src/main
│   ├── java/com/carmo/Vaultify
│   │   ├── Configuration/        # Configurações (beans de segurança / PasswordEncoder)
│   │   ├── Controller/           # Endpoints REST e renderização de páginas
│   │   ├── Dto/                  # Objetos de transferência de dados
│   │   ├── Service/              # Regras de negócio
│   │   ├── Validator/            # Validações (política de senha)
│   │   ├── Database/
│   │   │   ├── Entity/           # Entidades JPA (10 tabelas)
│   │   │   └── Repository/       # Repositórios Spring Data
│   │   └── VaultifyApplication.java
│   └── resources
│       ├── templates/            # Páginas Thymeleaf (login.html)
│       ├── static/               # CSS, JavaScript e imagens
│       └── application.properties
├── pom.xml
└── mvnw / mvnw.cmd
```

**Fluxo de uma requisição:** `Controller → Service → Repository → MySQL`

---

## 🗄 Modelo de dados

O diagrama abaixo descreve as tabelas, chaves primárias (PK), chaves estrangeiras (FK) e os relacionamentos entre os módulos de **usuários, contas, cartões, despesas, financiamentos e investimentos**.

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

## 🧠 Decisões de projeto

**1. Separação dos tipos de gasto**
- **Transações** — gastos variáveis. Podem ser lançadas direto em uma conta (`BILL`) ou vinculadas à fatura de um cartão de crédito (`CREDIT_CARD_STATEMENT`).
- **Contas fixas** — tabela própria (`FIXED_BILLS`) que funciona como modelo do que é cobrado periodicamente, com frequência e flag de ativação.
- **Financiamentos** — entidade desacoplada (`FINANCING`) que gera N parcelas (`FINANCING_INSTALLMENTS`), permitindo acompanhar o status de cada uma individualmente.

**2. Módulo isolado de investimentos**
A tabela `INVESTMENTS` guarda posição, preço médio e valor atual sem se misturar com as transações do dia a dia, o que facilita cálculos de rentabilidade no futuro.

**3. Precisão monetária**
Todos os valores financeiros usam `BigDecimal`, evitando os erros de arredondamento de `float`/`double`. A quantidade de ativos também é `BigDecimal`, preparada para frações de cotas.

**4. Identificadores e performance**
Chaves primárias do tipo **UUID** e relacionamentos `@ManyToOne` com carregamento **LAZY**.

**5. Isolamento por usuário**
Contas, cartões, categorias, financiamentos e investimentos pertencem a um usuário (`USER_ID`), deixando o modelo preparado para multiusuário.

**6. Configuração fora do código**
Credenciais do banco são lidas de **variáveis de ambiente**, sem dados sensíveis no repositório.

---

## Como Funciona a Autenticação com <strong>JWT</strong>
| Classe | Oque faz |
| ------ | -------- |
|`JwtService` | Cria e valida o token | 
|`JwtAuthenticatorFilter` | Controla cada request|
|`SecurityConfig`| Define as regras de segurança|
|`AuthController`|Recebe login e entrada de token          |

## Como tudo isso funciona na pratica?
`JWtService` Cria o token apartir de uma `SECRET_KEY` enviada junto ao .env, deixando sua assinatura, data de criação e expiração do token e implementando metodos para verificar os mesmos. <br>
1. Cria a chave em Base64.
2. Transforma a chave de Base64 para um array de bytes.
3. transforma o array de bytes em um SecretKey utilizando o algoritcmo hmac.
4. Cria token com a assinatura da chave ja criptografada.
5. Verifica se o token é valido apartir dos claims dentro do payload.
<details>
<summary>Código</summary>

````java
import com.carmo.Vaultify.Dto.UsersDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${SECRET_KEY}") //recebe a chave do .env
    private String SECRET_KEY;

    //criar o metodo para gerar as secret_key
    private SecretKey generateKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //criar metodo para gerar token assinados com a chave criada anteriormente
    public String generateToken(String email) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) //   expira o token em 2 horas
                .signWith(generateKey())
                .compact();
    }

    //extrair os claims do token -> declaracoes do payload
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //metodos para retornar qualque claims
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean tokenExpirated(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean tokenValid(String token, UserDetails userDetails) {
        try {
            String subject = extractSubject(token);
            return subject.equals(userDetails.getUsername()) && !tokenExpirated(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
````
</details>

`JwtAuthenticatorFilter` Recebe cada requisição e define como rotas  seguras ou publicas, interceptando cada requisição e verificando o payload <br>
1. Verifica se o header e sem nao veio vazio sem o `Authorization`  Se não so prossegue sem autenticar.
2. Verifica se o tokem começa com `Bearer ` para extrair o tokem do header. Se não so prossegue sem autenticar.
3. Tenta extrair o email do token utilizando o `jwtService` instanciado. Se não so prossegue sem autenticar.
4. Tenta achar um usuario com `userDetailsService` instanciado. Se não so prossegue sem autenticar.
5. valida o usuario.
<details>
<summary>Código</summary>

```java
import com.carmo.Vaultify.Service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class JwtAuthenticatorFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwtToken = authHeader.substring(7);

        String email;
        try {
            email = jwtService.extractSubject(jwtToken);
        } catch (JwtException | IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        }
        //verifica se o token realmente tem um email dentro

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) { //  SecurityContextHolder.getContext().getAuthentication() == null -> verifica se veio realmete um email e se o usuario ainda nao foi autenticado.
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(email);
            } catch (UsernameNotFoundException e) {
                filterChain.doFilter(request, response);
                return;
            }
            if (jwtService.tokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }


        } 
        filterChain.doFilter(request, response);


    }

}
```
</details>

`SecurityConfig` 

## 🔌 Endpoints

| Método | Rota | Descrição | Retorno |
|---|---|---|---|
| `GET` | `/login` | Renderiza a página de login | Página HTML |
| `PUT` | `/api/login` | Cadastra um novo usuário (valida senha e aplica hash BCrypt) | `201 Created` · `400 Bad Request` |
| `POST` | `/api/login` | Consulta o usuário por e-mail e valida as credenciais *(em evolução)* | `boolean` |

**Exemplo — cadastro de usuário**

```http
PUT /api/login
Content-Type: application/json

{
  "name": "Maria Silva",
  "email": "maria@email.com",
  "hashPwd": "Senha@123"
}
```

---

## 🚀 Como executar

### Pré-requisitos

- **Java 21**
- **MySQL** em execução
- Maven (opcional — o projeto já inclui o Maven Wrapper)

### 1. Clone o repositório

```bash
git clone https://github.com/iamytz/Vaultify.git
cd Vaultify
git checkout core
```

### 2. Configure as variáveis de ambiente

| Variável | Exemplo |
|---|---|
| `DATABASE_URL` | `jdbc:mysql://localhost:3306/vaultify?createDatabaseIfNotExist=true` |
| `DATABASE_USERNAME` | `root` |
| `DATABASE_PWD` | `sua_senha` |

**Linux / macOS**
```bash
export DATABASE_URL="jdbc:mysql://localhost:3306/vaultify?createDatabaseIfNotExist=true"
export DATABASE_USERNAME="root"
export DATABASE_PWD="sua_senha"
```

**Windows (PowerShell)**
```powershell
$env:DATABASE_URL="jdbc:mysql://localhost:3306/vaultify?createDatabaseIfNotExist=true"
$env:DATABASE_USERNAME="root"
$env:DATABASE_PWD="sua_senha"
$env:SECRET_KEY="sua_secret_key"
```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run      # Linux / macOS
mvnw.cmd spring-boot:run    # Windows
```

As tabelas são criadas automaticamente pelo Hibernate na primeira execução. A aplicação sobe em **http://localhost:8080** e a tela de login fica em `/login`.

---

## 🗺 Próximos passos

- [x] Modelagem completa do banco de dados
- [x] Cadastro de usuário com política de senha e hash
- [x] Página de login
- [ ] Autenticação completa de usuários
- [ ] Camadas de serviço e API para contas, cartões, transações, financiamentos e investimentos
- [ ] Telas de gestão financeira

---

## 👨‍💻 Autor

Desenvolvido por **[iamytz](https://github.com/iamytz)**.

Projeto em constante evolução — sugestões e feedbacks são bem-vindos!!
<div align="center">

<a href="https://github.com/iamytz">
<img src="https://img.shields.io/badge/GitHub-iamytz-181717?style=for-the-badge&logo=github" />
</a>
<a href="https://www.linkedin.com/in/gabriel-carmo-pe/">
<img src="https://img.shields.io/badge/LinkedIn-Gabriel%20Carmo-0A66C2?style=for-the-badge&logo=linkedin" />
</a>

<br> <br>

⭐ *Se você gostou do Vaultify, deixe uma estrela no repositório!*
</div>


<div align="center">

Desenvolvido com dedicação, aprendizado contínuo e foco em evolução técnica.

</div>