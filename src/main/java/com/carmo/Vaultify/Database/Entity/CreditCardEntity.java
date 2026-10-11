package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREDIT_CARD")
public class CreditCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME",nullable = false)
    private String name;

    @Column(name = "TOTAL_LIMIT",nullable = false)
    private BigDecimal totalLimit;

    @Column(name = "CLOSING_DAY",nullable = false)  //dia de fechamento da fatura
    private LocalDate closingDay;

    @Column(name = "DUE_DAY",nullable = false)  //dia de pagamento da fatura
    private LocalDate dueDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @OneToMany(mappedBy = "creditCard")
    private List<CreditCardStatementEntity> creditCardStatements;





}
