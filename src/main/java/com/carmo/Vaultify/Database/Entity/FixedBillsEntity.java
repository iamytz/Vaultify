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
@Table(name = "FIXED_BILLS")
public class FixedBillsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "VALUE",nullable = false)
    private BigDecimal value;

    @Column(name = "DUE_DAY",nullable = false)
    private LocalDate dueDay;

    @Column(name = "FREQUENCY",nullable = false)
    private String frequency;

    @Column(name = "IS_ACTIVE",nullable = false)
    private boolean Isactive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BILL_ID")
    private BillEntity bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity category;



}
