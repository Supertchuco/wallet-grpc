package com.wallet.walletserver.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AmountIsZeroException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AmountIsZeroException(final String message) {
        super(message);
    }
}