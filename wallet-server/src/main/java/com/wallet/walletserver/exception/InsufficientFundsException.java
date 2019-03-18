package com.wallet.walletserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InsufficientFundsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientFundsException(final String message) {
        super(message);
    }
}