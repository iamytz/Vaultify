package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREDIT_CARD_STATEMENT")
public class CreditCardStatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;


    @Column(name = "MONTH",nullable = false)
    private Integer month;

    @Column(name = "YEAR",nullable = false)
    private Integer year;

    @Column(name = "TOTAL_VALUE",nullable = false)
    private BigDecimal totalValue;

    @Column(name = "STATUS",nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDIT_CARD_ID")
    private CreditCardEntity creditCard;

    @OneToMany(mappedBy = "creditCardStatement")
    private List<TransactionEntity> transactions;


}
