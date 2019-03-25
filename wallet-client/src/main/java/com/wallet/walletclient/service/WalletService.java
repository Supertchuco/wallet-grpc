package com.wallet.walletclient.service;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.StatusMessage;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.exception.AmountShouldBeGreaterThanZeroException;
import com.wallet.walletclient.exception.InsufficientBalanceException;
import com.wallet.walletclient.exception.InvalidArgumentsException;
import com.wallet.walletclient.exception.UnknowException;
import com.wallet.walletclient.exception.UserNotFoundException;
import com.wallet.walletclient.grpcservice.WalletClientGrpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletService {

    @Autowired
    private WalletClientGrpcService walletClientGrpcService;

    public String depositClientOperation(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                         final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        String status = StringUtils.EMPTY;
        try {
            log.info("Initiate deposit client Operation");
            status = walletClientGrpcService.deposit(futureStub, baseRequest, taskExecutor).get().getStatus().name();
        } catch (Exception e) {
            log.error("Error during deposit client operation", e);
            throw new UnknowException("Internal server error");
        }
        return status;
    }

    public String withdrawClientOperation(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                          final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        String status = StringUtils.EMPTY;
        try {
            log.info("Initiate withdraw client Operation");
            status = walletClientGrpcService.withdraw(futureStub, baseRequest, taskExecutor).get().getStatus().name();
        } catch (Exception e) {
            log.error("Error during withdraw client operation", e);
            throwSpecificException(e);
        }
        return status;
    }

    public String getBalanceClientOperation(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                            final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        String balance = StringUtils.EMPTY;
        try {
            log.info("Initiate getBalance client Operation");
            balance = walletClientGrpcService.balance(futureStub, baseRequest, taskExecutor).get().getStatusMessage();
        } catch (Exception e) {
            throwSpecificException(e);
        }
        return balance;
    }

    private void throwSpecificException(final Exception exception) {
        String message = ExceptionUtils.getRootCauseMessage(exception);
        if (StringUtils.contains(exception.getMessage(), StatusMessage.USER_DOES_NOT_EXIST.toString())) {
            log.error("User not found");
            throw new UserNotFoundException(message);
        }
        if (StringUtils.contains(exception.getMessage(), StatusMessage.INVALID_ARGUMENTS.toString())) {
            log.error("Invalid arguments");
            throw new InvalidArgumentsException(message);
        }
        if (StringUtils.contains(exception.getMessage(), StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.toString())) {
            log.error("Amount should be greater than zero");
            throw new AmountShouldBeGreaterThanZeroException(message);
        }
        if (StringUtils.contains(exception.getMessage(), StatusMessage.INSUFFICIENT_BALANCE.toString())) {
            log.error("Insufficient balance");
            throw new InsufficientBalanceException(message);
        } else {
            log.error("Unknow exception ", exception);
            throw new UnknowException("Internal server error");
        }
    }
}
