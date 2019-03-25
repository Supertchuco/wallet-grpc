package com.wallet.walletclient.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InsufficientBalanceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientBalanceException(final String message) {
        super(message);
    }
}