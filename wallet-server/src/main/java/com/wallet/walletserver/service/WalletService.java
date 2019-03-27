package com.wallet.walletserver.service;


import com.google.gson.Gson;
import com.wallet.proto.CURRENCY;
import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.entity.Wallet;
import com.wallet.walletserver.exception.AmountIsZeroException;
import com.wallet.walletserver.exception.InsufficientFundsException;
import com.wallet.walletserver.vo.WalletBalance;
import com.wallet.walletserver.vo.WalletsBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WalletService {

    @Autowired
    private UserService userService;

    private void checkIfAmountIsZero(final BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 1) {
            log.error("Amount is zero");
            throw new AmountIsZeroException("Amount is zero");
        }
    }

    private void checkIfBalanceIsEnoughToWithdrawOperation(final BigDecimal withdrawValue, final BigDecimal balanceValue) {
        if (withdrawValue.compareTo(balanceValue) == -1) {
            log.error("Insufficient funds to withdraw operation");
            throw new InsufficientFundsException("Insufficient funds to withdraw operation");
        }
    }

    public void depositOperation(final BigDecimal depositValue, final int userId, final CURRENCY currency) {
        log.info("Initiate deposit Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        checkIfAmountIsZero(depositValue);
        user.getWalletByCurrency(currency.name()).setBalance(user.getWalletByCurrency(currency.name()).getBalance().add(depositValue));
        userService.saveUser(user);
    }

    public void withdrawOperation(final BigDecimal withdrawValue, final int userId, final CURRENCY currency) {
        log.info("Initiate withdraw Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        checkIfAmountIsZero(withdrawValue);
        checkIfBalanceIsEnoughToWithdrawOperation(withdrawValue, user.getWalletByCurrency(currency.name()).getBalance());
        user.getWalletByCurrency(currency.name()).setBalance(user.getWalletByCurrency(currency.name()).getBalance().subtract(withdrawValue));
        userService.saveUser(user);
    }

    public String getWalletsBalance(final int userId) {
        log.info("Initiate get balance Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);

        WalletBalance walletBalance = null;
        List<WalletBalance> walletBalanceList = new ArrayList<>();
        for (Wallet currentWallet : user.getWallets()) {
            walletBalanceList.add(new WalletBalance(currentWallet.getCurrency().name(), walletBalance.getBalance()));
        }

        return new Gson().toJson(new WalletsBalance(walletBalanceList));
    }

}
