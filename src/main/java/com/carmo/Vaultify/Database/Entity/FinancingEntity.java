package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "FINANCING")
public class FinancingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "DESCRIPTION",nullable = false)
    private String description;

    @Column(name = "TOTAL_VALUE",nullable = false)
    private BigDecimal totalValue;

    @Column(name = "ANNUAL_INTEREST_RATE",nullable = false)  //  TAXA DE JUROS ANUAL
    private BigDecimal annualInterestRate;

    @Column(name = "TOTAL_INSTALLMENTS",nullable = false)
    private Integer totalInstallments;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID",nullable = false)
    private UsersEntity user;

    @OneToMany(mappedBy = "financing")
    private List<FinancingInstallmentsEntity> financingInstallments;


}
