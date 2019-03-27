package com.wallet.walletclient.vo;

import com.wallet.proto.CURRENCY;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DepositRequestVO {

    private int userId;

    private String amount;

    private CURRENCY currency;
}
