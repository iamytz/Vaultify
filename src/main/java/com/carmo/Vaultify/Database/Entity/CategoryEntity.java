package com.carmo.Vaultify.Database.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME",nullable = false)
    private String name;

    @Column(name = "TYPE",nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UsersEntity user;

    @OneToMany(mappedBy = "category")
    private List<FixedBillsEntity> fixedBills;

    @OneToMany(mappedBy = "category")
    private List<TransactionEntity> transactions;

}
