package com.wallet.walletclient.service;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.StatusMessage;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.entity.Wallet;
import com.wallet.walletclient.exception.*;
import com.wallet.walletclient.grpcservice.WalletClientGrpcService;
import com.wallet.walletclient.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wallet.walletclient.enumerators.Error.*;

@Slf4j
@Service
public class WalletService {

    @Autowired
    private WalletClientGrpcService walletClientGrpcService;

    @Autowired
    private WalletRepository walletRepository;

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
            log.error(USER_NOT_FOUND.getMessage());
            throw new UserNotFoundException(USER_NOT_FOUND.getMessage());
        }
        if (StringUtils.contains(exception.getMessage(), StatusMessage.INVALID_ARGUMENTS.toString())) {
            log.error(INVALID_ARGUMENTS.getMessage());
            throw new InvalidArgumentsException(INVALID_ARGUMENTS.getMessage());
        }
        if (StringUtils.contains(exception.getMessage(), StatusMessage.AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.toString())) {
            log.error(AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.getMessage());
            throw new AmountShouldBeGreaterThanZeroException(AMOUNT_SHOULD_BE_GREATER_THAN_ZERO.getMessage());
        }
        if (StringUtils.contains(exception.getMessage(), StatusMessage.INSUFFICIENT_BALANCE.toString())) {
            log.error(INSUFFICIENT_FOUNDS.getMessage());
            throw new InsufficientBalanceException(INSUFFICIENT_FOUNDS.getMessage());
        } else {
            log.error("Unknow Exception", exception);
            throw new UnknowException(UNKNOW_EXCEPTION.getMessage());
        }
    }

    public void deleteWallets(final List<Wallet> wallets) {
        walletRepository.deleteAll(wallets);
    }

    public List<Wallet> findWalletsByUserId(final int userId){
        return walletRepository.findWalletByUserId(userId);
    }
}
