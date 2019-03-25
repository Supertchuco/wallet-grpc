package com.wallet.walletclient.service;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.STATUS;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.exception.UnknowException;
import com.wallet.walletclient.grpcservice.WalletClientGrpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletService {

    @Autowired
    private WalletClientGrpcService walletClientGrpcService;

    public STATUS depositClientOperation(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                         final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        try {
            log.info("Initiate deposit client Operation");
            return walletClientGrpcService.deposit(futureStub, baseRequest, taskExecutor).get().getStatus();
        } catch (Exception e) {
            log.error("Error during deposit client operation", e);
            throw new UnknowException("Internal server error");
        }
    }

    public STATUS withdrawClientOperation(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                          final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        try {
            log.info("Initiate withdraw client Operation");
            return walletClientGrpcService.withdraw(futureStub, baseRequest, taskExecutor).get().getStatus();
        } catch (Exception e) {
            log.error("Error during withdraw client operation", e);
            throw new UnknowException("Internal server error");
        }
    }

    public String getBalanceClientOperation(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                                            final BaseRequest baseRequest, final TaskExecutor taskExecutor) {
        try {
            log.info("Initiate getBalance client Operation");
            return walletClientGrpcService.balance(futureStub, baseRequest, taskExecutor).get().getStatusMessage();
        } catch (Exception e) {
            log.error("Error during getBalance client operation", e);
            throw new UnknowException("Internal server error");
        }
    }
}
