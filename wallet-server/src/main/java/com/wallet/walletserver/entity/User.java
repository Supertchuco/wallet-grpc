package com.wallet.walletserver.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "id", nullable = false)
    private List<Wallet> wallets;

    public Wallet getWalletByCurrency(final String currency) {
        Wallet walletReturn = null;
        for (int index = 0; index < wallets.size(); index++) {
            if (StringUtils.equals(wallets.get(index).getCurrency().name(), currency)) {
                walletReturn = wallets.get(index);
                break;
            }
        }
        return walletReturn;
    }
}
