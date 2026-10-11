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
@Table(name = "INVESTMENTS")
public class InvestmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "ASSET_CODE",nullable = false) //se torna unico pois nao podem ter 2 investimentos iguais, eles dever ser somados em um só
    private String assetCode;

    @Column(name = "ASSET_TYPE",nullable = false)
    private String assetType;

    @Column(name = "BROKER",nullable = false)
    private String broker;

    @Column(name = "QUANTITY",nullable = false)
    private BigDecimal quantity;

    @Column(name = "AVERAGE_PRICE",nullable = false)
    private BigDecimal averagePrice;

    @Column(name = "CURRENT_VALUE",nullable = false)
    private BigDecimal currentValue;

    @Column(name = "LAST_CONTRIBUTION_DATE",nullable = false)
    private LocalDate lastContributionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;






}
