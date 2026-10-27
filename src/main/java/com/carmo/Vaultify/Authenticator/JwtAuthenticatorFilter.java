package com.carmo.Vaultify.Authenticator;

import com.carmo.Vaultify.Service.JwtService;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
@Getter
@Setter
public class JwtAuthenticatorFilter extends OncePerRequestFilter {
    //  INJEÇÃO DE DEPENDENCIA
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserDetails userDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        //carregando o usuario do banco de dados
        String jwtToken = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractSubject(jwtToken);
        }catch (JwtException | IllegalArgumentException e ) {
            filterChain.doFilter(request,response);
            return;
            //  tenta capturaro email de dentro do token, se nao conseguir nao continua a execução
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {//    verfifica se o token realmente trouxe um email e so carrega o usuario caso ele ainda nao esteja authenticado evitando procurar atoa
           UserDetails userDetails;
            try {
               userDetails = userDetailsService.loadUserByUsername(email);//  carrega o usuario como UserDetails
           }catch (UsernameNotFoundException e) {
                return;
            }


            if (jwtService.tokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());// cria um objeto do SpringSecurity que entende como usuario authenticado, que recebe 3 argumentos, o usuario, null(senha vazia pois o usuario ja foi autenticado via token) e as permissoes userDetails.getAuthorities
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));// anexa as informacoe da requisicao(ip,sessao) na autenticacao util par aauditoria
                SecurityContextHolder.getContext().setAuthentication(authToken);//  guarda a autenticacao no contexto da requisicao atual, que faz o spring security librar acesso e permite que o controlle usar a Annotation @AuthenticationPrincipal ou @Authenticartion
            }

        }filterChain.doFilter(request,response); //prossegue com a requisição ja authenticada ao controller


}

}

