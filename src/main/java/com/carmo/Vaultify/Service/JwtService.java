package com.carmo.Vaultify.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${SECRET_KEY}")
    private  String SECRET_KEY;

    //CRIANDO A CHAVE PARA ASSINATURA DE TOKEN
    public SecretKey generateKey() {
        byte[] keybytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keybytes);
    }

    //  GERANDO O TOKEN
    public String generateToken(String email) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) //autentica por 2 horas
                .signWith(generateKey())
                .compact();
    }

    //extrai os claims
    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    public String extractSubject(String token) {
        return extractClaims(token,Claims::getSubject);
    }

}
