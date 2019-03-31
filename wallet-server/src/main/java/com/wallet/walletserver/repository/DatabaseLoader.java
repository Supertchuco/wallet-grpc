package com.wallet.walletserver.repository;

import com.wallet.proto.CURRENCY;
import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public DatabaseLoader(UserRepository userRepo, WalletRepository walletRepo) {
        this.userRepository = userRepo;
        this.walletRepository = walletRepo;
    }

    @Override
    public void run(String... strings) {

        List<Wallet> wallets = new ArrayList<>();
        wallets.add(new Wallet(1, new BigDecimal("24.45"), CURRENCY.USD));
        wallets.add(new Wallet(2, new BigDecimal("18.22"), CURRENCY.GBP));
        User user1 = new User(1, "Rodrigo Suco", wallets);
        userRepository.save(user1);

        wallets = new ArrayList<>();
        wallets.add(new Wallet(3, new BigDecimal("0.00"), CURRENCY.UNRECOGNIZED));
        User user2 = new User(2, "Andre Balada", wallets);
        userRepository.save(user2);

        wallets = null;
        User user3 = new User(3, "Vizeu", wallets);
        userRepository.save(user3);
    }

}