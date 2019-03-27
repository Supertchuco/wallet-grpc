package com.wallet.walletserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletBalance {

    private String currency;

    private String balance;
}
