package com.wallet.walletserver.service;

import com.wallet.proto.CURRENCY;
import com.wallet.walletserver.exception.InvalidCurrencyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyService {

    public void validateCurrency(final CURRENCY currency) {
        if (currency.equals(CURRENCY.UNRECOGNIZED)) {
            log.error("Invalid currency: {}", currency.name());
            throw new InvalidCurrencyException("Currency not recognized: " + currency.name());
        }
    }

}
