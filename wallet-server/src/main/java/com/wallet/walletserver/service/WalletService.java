package com.wallet.walletserver.service;


import com.google.gson.Gson;
import com.wallet.proto.CURRENCY;
import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.entity.Wallet;
import com.wallet.walletserver.enumerator.Operation;
import com.wallet.walletserver.exception.AmountIsZeroException;
import com.wallet.walletserver.exception.InsufficientFundsException;
import com.wallet.walletserver.exception.InvalidArgumentException;
import com.wallet.walletserver.exception.OperationNotRecognizedException;
import com.wallet.walletserver.vo.WalletBalance;
import com.wallet.walletserver.vo.WalletsBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    public User depositOperation(final BigDecimal depositValue, final int userId, final CURRENCY currency) {
        log.info("Initiate deposit Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        checkIfAmountIsZero(depositValue);
        if (userService.userHasCurrencyWallet(user, currency)) {
            user.getWalletByCurrency(currency.name()).setBalance(updateWalletBalanceValue(user.getWalletByCurrency(currency.name()).getBalance(), depositValue, Operation.DEPOSIT.name()));
        } else {
            log.info("Create new wallet with currency {}", currency);
            if(Objects.isNull(user.getWallets())){
                user.setWallets(new ArrayList<>());
            }
            user.getWallets().add(new Wallet(depositValue, currency));
        }
        return userService.saveUser(user);
    }

    public void withdrawOperation(final BigDecimal withdrawValue, final int userId, final CURRENCY currency) {
        log.info("Initiate withdraw Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        checkIfAmountIsZero(withdrawValue);
        if (userService.userHasCurrencyWallet(user, currency)) {
            checkIfBalanceIsEnoughToWithdrawOperation(withdrawValue, user.getWalletByCurrency(currency.name()).getBalance());
            user.getWalletByCurrency(currency.name()).setBalance(updateWalletBalanceValue(user.getWalletByCurrency(currency.name()).getBalance(), withdrawValue, Operation.WITHDRAW.name()));
            userService.saveUser(user);
        }else{
            throw new InsufficientFundsException();
        }
    }

    public String getWalletsBalance(final int userId) {
        log.info("Initiate get balance Operation");
        User user = userService.findUserById(userId);
        userService.validateUser(user);
        List<WalletBalance> walletBalanceList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(user.getWallets())) {
            for (Wallet currentWallet : user.getWallets()) {
                walletBalanceList.add(new WalletBalance(currentWallet.getCurrency().name(),
                        currentWallet.getBalance().toString()));
            }
        }
        return new Gson().toJson(new WalletsBalance(walletBalanceList));
    }

    private BigDecimal updateWalletBalanceValue(final BigDecimal balance, final BigDecimal operationValue, final String operation) {
        log.info("Update wallet balance value");
        BigDecimal updatedValue;
        if (StringUtils.equals(operation, Operation.WITHDRAW.name())) {
            updatedValue = balance.subtract(operationValue);
        } else if (StringUtils.equals(operation, Operation.DEPOSIT.name())) {
            updatedValue = balance.add(operationValue);
        } else {
            log.info("Operation not recognized");
            throw new OperationNotRecognizedException();
        }
        return updatedValue;
    }

}
