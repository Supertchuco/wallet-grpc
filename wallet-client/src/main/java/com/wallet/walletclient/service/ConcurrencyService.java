package com.wallet.walletclient.service;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.CURRENCY;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.entity.User;
import com.wallet.walletclient.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ConcurrencyService {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    private static final char A = 'A';
    private static final char B = 'B';
    private static final char C = 'C';

    private void doRoundA(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                          final int userId, final TaskExecutor taskExecutor) {
        log.info("Initiate Round A for user id: {}", userId);
        walletService.depositClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "200.00"), taskExecutor);
        walletService.depositClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.EUR, "100.00"), taskExecutor);
        walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
    }

    private void doRoundB(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                          final int userId, final TaskExecutor taskExecutor) {
        log.info("Initiate Round B for user id: {}", userId);

        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        walletService.depositClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "300.00"), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);

    }

    private void doRoundC(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                          final int userId, final TaskExecutor taskExecutor) {
        log.info("Initiate Round C for user id: {}", userId);
        walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        walletService.depositClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        walletService.depositClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        walletService.depositClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "100.00"), taskExecutor);
        walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
        walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.USD, "200.00"), taskExecutor);
        walletService.getBalanceClientOperation(futureStub, BaseRequest.newBuilder().setUserID(userId).build(), taskExecutor);
    }

    private BaseRequest buildBaseRequest(final int userId, final CURRENCY currency, final String amount) {
        return BaseRequest.newBuilder().setUserID(userId).setAmount(amount).setCurrency(currency).build();
    }


    public void startUsersConcurrently(final int numberUsers, final int concurrentThreadsPerUser, final int numberOfRoundsPerThred) {

        List<User> users = new ArrayList<>();
        log.info("create the users on database");
        for (int index = 0; index < numberUsers; index++) {
            users.add(new User(index, "name_" + index, null));
            userRepository.save(users.get(index));
        }

        // create the threads
        // randomize the rounds per thread
    }

    private char randomizeRounds() {
        char[] arr = {A, B, C};
        Random random = new Random();
        return arr[random.nextInt(arr.length)];
    }

    public void executeRound(final WalletServiceGrpc.WalletServiceFutureStub futureStub,
                             final int userId, final TaskExecutor taskExecutor) {
        char round = randomizeRounds();
        if (round == A) {
            doRoundA(futureStub, userId, taskExecutor);
        } else if (round == B) {
            doRoundB(futureStub, userId, taskExecutor);
        } else {
            doRoundC(futureStub, userId, taskExecutor);
        }
    }


/*    The wallet client should have the following CLI parameters:
    users (number of concurrent users emulated)
    concurrent_threads_per_user (number of concurrent requests a user will make)
    rounds_per_thread (number of rounds each thread is executing)
    Make sure the client exits when all rounds has been executed.*/
}
