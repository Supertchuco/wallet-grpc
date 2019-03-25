package com.wallet.walletclient.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidArgumentsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidArgumentsException(final String message) {
        super(message);
    }
}