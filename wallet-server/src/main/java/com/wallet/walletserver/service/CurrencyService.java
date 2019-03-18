package com.wallet.walletserver.service;

import com.wallet.proto.CURRENCY;
import com.wallet.walletserver.exception.InvalidCurrencyException;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    public void validateCurrency(final CURRENCY currency) {
        if (currency.equals(CURRENCY.UNRECOGNIZED)) {
            throw new InvalidCurrencyException("Currency not recognized: " + currency.name());
        }
    }

}
