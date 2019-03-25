package com.wallet.walletclient.controller;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.CURRENCY;
import com.wallet.proto.STATUS;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletServiceGrpc.WalletServiceFutureStub walletServiceFutureStub;

    @Autowired
    private TaskExecutor taskExecuter;

    @Autowired
    private WalletService walletService;

    @GetMapping(path = "/deposit")
    public STATUS deposit(@RequestParam(value = "userid") int userID, @RequestParam(value = "amount") String amount,
                          @RequestParam(value = "currency") CURRENCY currency) throws InterruptedException, ExecutionException {
        return walletService.depositClientOperation(walletServiceFutureStub,
            BaseRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build(),
            taskExecuter);
    }

    @GetMapping(path = "/withdraw")
    public STATUS withdraw(@RequestParam(value = "userid") int userID, @RequestParam(value = "amount") String amount,
                           @RequestParam(value = "currency") CURRENCY currency) throws InterruptedException, ExecutionException {

        return walletService.withdrawClientOperation(walletServiceFutureStub,
            BaseRequest.newBuilder().setUserID(userID).setAmount(amount).setCurrency(currency).build(),
            taskExecuter);
    }

    @GetMapping(path = "/balance")
    public String withdraw(@RequestParam(value = "userid") int userID)
        throws InterruptedException, ExecutionException {
        return walletService.getBalanceClientOperation(walletServiceFutureStub, BaseRequest.newBuilder().setUserID(userID).build(),
            taskExecuter);
    }
}
