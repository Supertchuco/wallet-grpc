package com.wallet.walletclient.enumerators;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {

    USER_NOT_FOUND("User not found"),
    INVALID_ARGUMENTS("Invalid arguments"),
    AMOUNT_SHOULD_BE_GREATER_THAN_ZERO("Amount should be greater than zero"),
    UNKNOW_EXCEPTION("Internal server error. Please contact support"),
    INSUFFICIENT_BALANCE("Insufficient balance");

    private String message;

}
