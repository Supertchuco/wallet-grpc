package com.wallet.walletserver.repository;

import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
        Wallet wallet1 = new Wallet(1, new BigDecimal("24.45"));
        User user1 = new User(1, "Rodrigo Suco", wallet1);
        userRepository.save(user1);

        Wallet wallet2 = new Wallet(2, new BigDecimal("0.00"));
        User user2 = new User(2, "Andre Balada", wallet2);
        userRepository.save(user2);
    }

}