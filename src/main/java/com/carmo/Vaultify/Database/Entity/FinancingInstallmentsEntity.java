package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FINANCING_INSTALLMENTS")
public class FinancingInstallmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "INSTALLMENT_NUMBER",nullable = false)
    private Integer installmentNumber;

    @Column(name = "INSTALLMENT_PRICE",nullable = false)
    private BigDecimal installmentPrice;

    @Column(name = "DUE_DATE",nullable = false)
    private LocalDate dueDate;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FINANCING_ID")
    private FinancingEntity financing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BILL_ID")
    private BillEntity bill;

}
