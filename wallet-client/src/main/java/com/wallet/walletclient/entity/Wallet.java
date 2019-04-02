package com.wallet.walletclient.entity;

import com.wallet.proto.CURRENCY;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

}
