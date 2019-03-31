package com.wallet.walletclient.controller;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.service.WalletService;
import com.wallet.walletclient.vo.BalanceRequestVO;
import com.wallet.walletclient.vo.DepositRequestVO;
import com.wallet.walletclient.vo.WithdrawRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletServiceGrpc.WalletServiceFutureStub walletServiceFutureStub;

    @Autowired
    private TaskExecutor taskExecuter;

    @Autowired
    private WalletService walletService;

    @PostMapping(path = "/deposit")
    public String deposit(@RequestBody final DepositRequestVO depositRequestVO) {
        return walletService.depositClientOperation(walletServiceFutureStub,
                BaseRequest.newBuilder().setUserID(depositRequestVO.getUserId()).setAmount(depositRequestVO.getAmount()).setCurrency(depositRequestVO.getCurrency()).build(),
                taskExecuter);
    }

    @PostMapping(path = "/withdraw")
    public String withdraw(@RequestBody final WithdrawRequestVO withdrawRequestVO) {
        return walletService.withdrawClientOperation(walletServiceFutureStub,
                BaseRequest.newBuilder().setUserID(withdrawRequestVO.getUserId()).setAmount(withdrawRequestVO.getAmount()).setCurrency(withdrawRequestVO.getCurrency()).build(),
                taskExecuter);
    }

    @GetMapping(path = "/balance")
    public String balance(final BalanceRequestVO balanceRequestVO) {
        return walletService.getBalanceClientOperation(walletServiceFutureStub, BaseRequest.newBuilder().setUserID(balanceRequestVO.getUserId()).build(),
                taskExecuter);
    }
}
