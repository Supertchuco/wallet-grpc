package com.wallet.walletserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidCurrencyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidCurrencyException(final String message) {
        super(message);
    }
}