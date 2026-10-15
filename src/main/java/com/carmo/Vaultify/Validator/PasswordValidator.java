package com.carmo.Vaultify.Validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public boolean validarSenha(String senha) {
        //VERIFICAR SE A SENHA TEM 8 CARACTERES
        //VERIFICAR SE TEM CARACTERES ESPECIAIS
        //VEFIFICAR SE TEM MAISCULA E MINUSCULA
        //VERIFICA SE TEM NUMERO

        if (senha == null || senha.length()<8) {    //a senha precisa no minimo de 8 caracteres
            return false;
        }
        boolean digito = false;
        boolean maiuscula = false;
        boolean minuscula = false;
        boolean especial = false;


        for (char c: senha.toCharArray()) {//laço for each
            if (Character.isDigit(c)) {
                digito = true;
            }
            if (Character.isUpperCase(c)) {
                maiuscula = true;
            }
            if (Character.isLowerCase(c)) {
                minuscula = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                especial = true;
            }
        }
        return digito && maiuscula && minuscula && especial;

    }




}
