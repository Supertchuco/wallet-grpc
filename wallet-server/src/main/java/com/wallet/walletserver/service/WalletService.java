package com.wallet.walletserver.service;


import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.exception.AmountIsZeroException;
import com.wallet.walletserver.exception.InsufficientFundsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
public class WalletService {

    @Autowired
    private UserService userService;

    private void checkIfAmountIsZero(final BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) < 1) {
            log.error("Amount is zero or null {}", amount);
            throw new AmountIsZeroException("Amount is zero");
        }
    }

    private void checkIfBalanceIsEnoughToWithdrawOperation(final BigDecimal withdrawValue, final BigDecimal balanceValue) {
        if (Objects.isNull(balanceValue) || withdrawValue.compareTo(balanceValue) == 1) {
            log.error("Insufficient funds to withdraw operation");
            throw new InsufficientFundsException("Insufficient funds to withdraw operation");
        }
    }

    public void depositOperation(final BigDecimal depositValue, final int userId) {
        log.info("Initiate deposit Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        checkIfAmountIsZero(depositValue);
        user.getWallet().setBalance(user.getWallet().getBalance().add(depositValue));
        userService.saveUser(user);
    }

    public void withdrawOperation(final BigDecimal withdrawValue, final int userId) {
        log.info("Initiate withdraw Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        checkIfAmountIsZero(withdrawValue);
        checkIfBalanceIsEnoughToWithdrawOperation(withdrawValue, user.getWallet().getBalance());
        user.getWallet().setBalance(user.getWallet().getBalance().subtract(withdrawValue));
        userService.saveUser(user);
    }

    public BigDecimal getBalance(final int userId) {
        log.info("Initiate get balance Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        return user.getWallet().getBalance();
    }

}
