package com.carmo.Vaultify.Service;

import com.carmo.Vaultify.Database.Entity.UsersEntity;
import com.carmo.Vaultify.Database.Repository.UsersRepository;
import com.carmo.Vaultify.Dto.UsersDto;
import com.carmo.Vaultify.Validator.PasswordValidator;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Getter
@Setter
@Builder
public class UsersService {
    private UsersRepository repository;
    private PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder; // so utilizar na hora de passar a senha
    }

    public boolean tentarLogin(UsersDto dto) {
        Optional<UsersEntity> entity = repository.findByEmail(dto.getEmail());
        if (entity.isEmpty()) {
            return false;
        }
        UsersEntity user = entity.get();
        return user.getHashPwd().equals(dto.getHashPwd());
    }

    public ResponseEntity<UsersEntity> createUser(UsersDto dto) {
        PasswordValidator senhaValida = new PasswordValidator();
        if (senhaValida.validarSenha(dto.getHashPwd())) {
            UsersEntity user = UsersEntity.builder().name(dto.getName()).email(dto.getEmail()).hashPwd(passwordEncoder.encode(dto.getHashPwd())).createdAt(LocalDateTime.now()).build();
            return new ResponseEntity<>(repository.save(user), HttpStatus.CREATED);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }





}
