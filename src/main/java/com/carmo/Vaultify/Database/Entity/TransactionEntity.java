package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTION")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "DESCRIPTION",nullable = false)
    private String description;

    @Column(name = "VALUE",nullable = false)
    private BigDecimal value;

    @Column(name = "TYPE",nullable = false)
    private String type;

    @Column(name = "TRANSACTION_DATE",nullable = false)
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BILL_ID")
    private BillEntity bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDIT_CARD_STATEMENT_ID")
    private CreditCardStatementEntity creditCardStatement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity category;

}
