package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BILL")
public class BillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME",nullable = false)
    private String name;

    @Column(name = "TYPE",nullable = false)
    private String type;

    @Column(name = "CURRENT_BALANCE",nullable = false)
    private BigDecimal currentBalance;

    @Column(name = "CURRENCY",nullable = false)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @OneToMany(mappedBy = "bill")
    private List<FinancingInstallmentsEntity> financingInstallments;

    @OneToMany(mappedBy = "bill")
    private List<FixedBillsEntity> fixedBills;

    @OneToMany(mappedBy = "bill")
    private List<TransactionEntity> transactions;


}
