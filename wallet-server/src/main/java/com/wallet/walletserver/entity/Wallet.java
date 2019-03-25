package com.wallet.walletserver.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity(name = "Wallet")
@Table(name = "Wallet")
@AllArgsConstructor
@NoArgsConstructor
public class Wallet implements Serializable {

    @Id
    @Column
    private int walletId;

    @Column
    private BigDecimal balance;
}
