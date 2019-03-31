package com.wallet.walletclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallet.proto.CURRENCY;
import lombok.Data;

import java.io.Serializable;

@Data
public class DepositRequestVO implements Serializable {

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("currency")
    private CURRENCY currency;
}
