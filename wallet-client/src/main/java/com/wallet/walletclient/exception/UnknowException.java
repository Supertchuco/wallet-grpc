package com.wallet.walletclient.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnknowException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnknowException(final String message) {
        super(message);
    }
}