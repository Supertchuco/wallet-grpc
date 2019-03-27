package com.wallet.walletclient.controller;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.service.WalletService;
import com.wallet.walletclient.vo.BalanceRequestVO;
import com.wallet.walletclient.vo.DepositRequestVO;
import com.wallet.walletclient.vo.WithdrawRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String deposit(final DepositRequestVO depositRequestVO) throws InterruptedException, ExecutionException {
        return walletService.depositClientOperation(walletServiceFutureStub,
            BaseRequest.newBuilder().setUserID(depositRequestVO.getUserId()).setAmount(depositRequestVO.getAmount()).setCurrency(depositRequestVO.getCurrency()).build(),
            taskExecuter);
    }

    @GetMapping(path = "/withdraw")
    public String withdraw(final WithdrawRequestVO withdrawRequestVO) throws InterruptedException, ExecutionException {
        return walletService.withdrawClientOperation(walletServiceFutureStub,
            BaseRequest.newBuilder().setUserID(withdrawRequestVO.getUserId()).setAmount(withdrawRequestVO.getAmount()).setCurrency(withdrawRequestVO.getCurrency()).build(),
            taskExecuter);
    }

    @GetMapping(path = "/balance")
    public String withdraw(final BalanceRequestVO balanceRequestVO)
        throws InterruptedException, ExecutionException {
        return walletService.getBalanceClientOperation(walletServiceFutureStub, BaseRequest.newBuilder().setUserID(balanceRequestVO.getUserId()).build(),
            taskExecuter);
    }
}
