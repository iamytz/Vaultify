package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USERS")
public class UsersEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME",nullable = false)
    private String name;

    @Column(name = "EMAIL",unique = true,nullable = false)
    private String email;

    @Column(name = "HASH_PWD",nullable = false)
    private String hashPwd;

    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "user")   //  MAPPEDBY DEVE APONTAR EXATAMENTE PARA O NOME DO CAMPO NA OUTRA ENTIDADE
    private List<FinancingEntity> financings;

    @OneToMany(mappedBy = "user")
    private List<BillEntity> bills;

    @OneToMany(mappedBy = "user")
    private List<CreditCardEntity> creditCards;

    @OneToMany(mappedBy = "user")
    private List<InvestmentsEntity> investments;

    @OneToMany(mappedBy = "user")
    private List<CategoryEntity> categorys;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return hashPwd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
