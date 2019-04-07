package com.wallet.walletclient.service;

import com.wallet.proto.BaseRequest;
import com.wallet.proto.CURRENCY;
import com.wallet.walletclient.entity.User;
import com.wallet.walletclient.entity.Wallet;
import com.wallet.walletclient.exception.UnknowException;
import com.wallet.walletclient.thread.RoundThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.wallet.walletclient.enumerators.Error.UNKNOW_EXCEPTION;

@Service
@Slf4j
public class ConcurrencyService {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoundThread roundThread;

    public BaseRequest buildBaseRequest(final int userId, final CURRENCY currency, final String amount) {
        return BaseRequest.newBuilder().setUserID(userId).setAmount(amount).setCurrency(currency).build();
    }

    private List<User> createUsers(final int numberUsers) {
        List<User> users = new ArrayList<>();
        log.info("create the users on database");
        for (int index = 0; index < numberUsers; index++) {
            users.add(new User("name_concurrency_" + index));
            userService.saveUser(users.get(index));
        }
        return users;
    }

    private CompletableFuture<Void> buildCompletableFutureList(final List<User> users, final int concurrentThreadsPerUser, final int numberOfRoundsPerThread) {
        List<CompletableFuture<String>> executeRoundsFutures = new ArrayList<>();
        log.info("Building completable future list");
        try {
            for (User user : users) {
                log.info("Execute threads for user id: {}", user.getUserId());
                Wallet wallet;
                for (int index2 = 0; index2 < concurrentThreadsPerUser; index2++) {
                    executeRoundsFutures.add(roundThread.executeRounds(user.getUserId(), numberOfRoundsPerThread, index2));
                }
            }
        } catch (Exception e) {
            log.error("Catastrophic error during build completable future list", e);
            throw new UnknowException(UNKNOW_EXCEPTION.getMessage());
        }

        return CompletableFuture.allOf(
                executeRoundsFutures.toArray(new CompletableFuture[executeRoundsFutures.size()])
        );
    }

    private void waitForAllThreadsFinish(final CompletableFuture<Void> allFutures) {
        log.info("Waiting for pending threads...");
        try {
            while (!allFutures.isDone()) {
                log.info("Waiting...");
                Thread.sleep(300);
            }
        } catch (Exception e) {
            log.error("Error during waiting for all pending threads", e);
            throw new UnknowException(UNKNOW_EXCEPTION.getMessage());
        }
    }

    public void startUsersConcurrency(final int numberOfUsers, final int concurrentThreadsPerUser, final int numberOfRoundsPerThread) {
        log.info("Start user concurrency process");
        List<User> users = createUsers(numberOfUsers);
        waitForAllThreadsFinish(buildCompletableFutureList(users, concurrentThreadsPerUser, numberOfRoundsPerThread));
        log.info("All threads processed");
        deleteAllUsers(users);
    }

    private void deleteAllUsers(final List<User> users) {
        log.info("Delete all users created for this concurrency process");
        for (User user : users) {
            deleteUser(user);
        }
    }

    private void deleteUser(final User user) {
        List<Wallet> wallets = walletService.findWalletsByUserId(user.getUserId());
        if (!CollectionUtils.isEmpty(wallets)) {
            walletService.deleteWallets(wallets);
        }
        userService.deleteUser(user);
    }
}
