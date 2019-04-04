package com.wallet.walletclient.service;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.CURRENCY;
import com.wallet.proto.WalletServiceGrpc;
import com.wallet.walletclient.entity.User;
import com.wallet.walletclient.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ConcurrencyService {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletServiceGrpc.WalletServiceFutureStub walletServiceFutureStub;

    @Autowired
    private TaskExecutor taskExecuter;

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

        try {
            walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (Exception e) {
            log.info("Round B: {}", e.getMessage());
        }

        try {
            walletService.depositClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "300.00"), taskExecutor);
        } catch (Exception e) {
            log.info("Round B: {}", e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (Exception e) {
            log.info("Round B: {}", e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (Exception e) {
            log.info("Round B: {}", e.getMessage());
        }

        try {
            walletService.withdrawClientOperation(futureStub, buildBaseRequest(userId, CURRENCY.GBP, "100.00"), taskExecutor);
        } catch (Exception e) {
            log.info("Round B: {}", e.getMessage());
        }

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

    private List<User> createUsers(final int numberUsers) {
        List<User> users = new ArrayList<>();
        log.info("create the users on database");

        for (int index = 0; index < numberUsers; index++) {
            users.add(new User(index, "name_" + index, null));
            userRepository.save(users.get(index));
        }

        return users;
    }

    private void deleteUsers(final List<User> users) {
        log.info("delete users on database");
        userRepository.deleteAll(users);
    }

    @Async
    public void startUsersConcurrently(final int numberOfUsers, final int concurrentThreadsPerUser, final int numberOfRoundsPerThread) {

        List<User> users = createUsers(numberOfUsers);

        for (User user : users) {
            log.info("Execute threads for user id: {}", user.getUserId());
            for (int index2 = 0; index2 < numberOfRoundsPerThread; index2++) {
                executeRounds(user.getUserId(), numberOfRoundsPerThread);
            }
        }

        deleteUsers(users);
    }

    private char randomizeRounds(final char[] options, final Random random) {
        return options[random.nextInt(options.length)];
    }

    @Async
    private void executeRounds(final int userId, final int times) {
        final char[] options = {A, B, C};
        final Random random = new Random();
        char round;
        for (int index = 0; index < times; index++) {
           // round = randomizeRounds(options, random);
            round = 'B';
            if (round == A) {
                doRoundA(walletServiceFutureStub, userId, taskExecuter);
            } else if (round == B) {
                doRoundB(walletServiceFutureStub, userId, taskExecuter);
            } else {
                doRoundC(walletServiceFutureStub, userId, taskExecuter);
            }
        }
    }




/*    The wallet client should have the following CLI parameters:
    users (number of concurrent users emulated)
    concurrent_threads_per_user (number of concurrent requests a user will make)
    rounds_per_thread (number of rounds each thread is executing)
    Make sure the client exits when all rounds has been executed.*/
}
