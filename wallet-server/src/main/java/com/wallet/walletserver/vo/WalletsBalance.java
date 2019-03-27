package com.wallet.walletserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WalletsBalance {

    List<WalletBalance> walletBalanceList;
}
