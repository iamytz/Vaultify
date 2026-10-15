package com.carmo.Vaultify.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UsersDto {
    private String name;
    private String email;
    private String hashPwd;

}
