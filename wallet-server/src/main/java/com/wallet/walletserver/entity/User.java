package com.wallet.walletserver.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "User")
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column
    @GeneratedValue
    private int userId;

    @Column
    private String userName;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "userId", nullable = false)
    private List<Wallet> wallets;

    public Wallet getWalletByCurrency(final String currency) {
        return wallets.stream().
                filter(p -> p.getCurrency().name().equals(currency)).
                findAny().orElse(null);
    }

    public User(final String userName) {
        this.userName = userName;
    }
}
