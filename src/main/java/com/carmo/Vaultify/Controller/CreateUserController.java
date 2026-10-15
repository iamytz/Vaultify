package com.carmo.Vaultify.Controller;

import com.carmo.Vaultify.Database.Entity.UsersEntity;
import com.carmo.Vaultify.Dto.UsersDto;
import com.carmo.Vaultify.Service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CreateUserController {

    private UsersService service;

    public CreateUserController(UsersService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public boolean tryLogin(@RequestBody UsersDto dto) {
        return service.tentarLogin(dto);
    }

    @PutMapping("/login")
    public ResponseEntity<UsersEntity> createUser(@RequestBody UsersDto dto) {
        return service.createUser(dto);
    }
}
