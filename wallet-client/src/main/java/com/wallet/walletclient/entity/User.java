package com.wallet.walletclient.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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
    private int userId;

    @Column
    private String userName;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "userId", nullable = false)
    private List<Wallet> wallets;

}
