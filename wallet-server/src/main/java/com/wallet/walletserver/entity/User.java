package com.wallet.walletserver.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity(name = "User")
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column
    private int id;

    @Column
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "id", nullable = false)
    private Wallet wallet;
}
