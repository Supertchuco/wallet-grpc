package com.wallet.walletserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidArgumentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidArgumentException(final String message) {
        super(message);
    }
}