package com.wallet.walletclient.thread;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.CURRENCY;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.exception.*;
import com.wallet.walletclient.service.ConcurrencyService;
import com.wallet.walletclient.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class RoundThread {

    @Autowired
    private WalletService walletService;

    @Autowired
    private ConcurrencyService concurrencyService;

    private static final char A = 'A';
    private static final char B = 'B';
    private static final char C = 'C';

    @Autowired
    private WalletServiceGrpc.WalletServiceFutureStub walletServiceFutureStub;

    @Autowired
    private TaskExecutor taskExecuter;

    private void doRoundA(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                          final int userId, final TaskExecutor taskExecutor, final int threadNumber) {
        log.info("Initiate Round A for user id: {}", userId);
        try {
            walletService.depositClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round A: {}", e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "200.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round A: {}", e.getMessage());
        }

        try {
            walletService.depositClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.EUR, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round A: {}", e.getMessage());
        }

        try {
            walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round A: {}", e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round A: {}", e.getMessage());
        }

        try {
            walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round A: {}", e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round A: {}", e.getMessage());
        }

    }

    private void doRoundB(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                          final int userId, final TaskExecutor taskExecutor, final int threadNumber) {
        log.info("Initiate Round B for user id: {}, thread number: {}", userId, threadNumber);
        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.error("Round B Step 1 user id: {} error: {}", userId, e.getMessage());
        }

        try {
            walletService.depositClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.GBP, "300.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.error("Round B Step 2 user id: {} error: {}", userId, e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.error("Round B Step 3 user id: {} error: {}", userId, e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.error("Round B Step 4 user id: {} error: {}", userId, e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.error("Round B Step 5 user id: {} error: {}", userId, e.getMessage());
        }
    }

    private void doRoundC(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                          final int userId, final TaskExecutor taskExecutor, final int threadNumber) {
        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }

        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.depositClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }

        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.depositClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }

        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }

        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.depositClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }

        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }

        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.withdrawClientOperation(futureStub, concurrencyService.buildBaseRequest(userId, CURRENCY.USD, "200.00"), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }

        log.info("Initiate Round C for user id: {}", userId);
        try {
            walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        } catch (UserNotFoundException | InvalidArgumentsException | AmountShouldBeGreaterThanZeroException |
                InsufficientBalanceException | UnknowException e) {
            log.info("Round C: {}", e.getMessage());
        }
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> executeRounds(final int userId, final int times, final int threadNumber) {
        final char[] options = {A, B, C};
        final Random random = new Random();
        char round;
        log.info("Execute rounds for user id: {" + userId + "} and thread number {" + threadNumber + "}");
        for (int index = 0; index < times; index++) {
            round = randomizeRounds(options, random);
            if (round == A) {
                doRoundA(walletServiceFutureStub, userId, taskExecuter, threadNumber);
            } else if (round == B) {
                doRoundB(walletServiceFutureStub, userId, taskExecuter, threadNumber);
            } else {
                doRoundC(walletServiceFutureStub, userId, taskExecuter, threadNumber);
            }
        }
        return CompletableFuture.completedFuture("Execute");
    }


    private char randomizeRounds(final char[] options, final Random random) {
        return options[random.nextInt(options.length)];
    }
}
