package com.wallet.walletserver.entity;

import com.wallet.proto.CURRENCY;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    @GeneratedValue
    @Column
    private int walletId;

    @Column
    private BigDecimal balance;

    @Column
    private CURRENCY currency;

    public Wallet(final BigDecimal balance, final CURRENCY currency) {
        this.balance = balance;
        this.currency = currency;
    }

}
