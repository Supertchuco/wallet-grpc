package com.wallet.walletclient.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AmountShouldBeGreaterThanZeroException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AmountShouldBeGreaterThanZeroException(final String message) {
        super(message);
    }
}