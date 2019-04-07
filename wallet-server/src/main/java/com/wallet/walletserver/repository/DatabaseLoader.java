package com.wallet.walletserver.repository;

import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public DatabaseLoader(UserService userServ) {
        this.userService = userServ;
    }

    @Override
    public void run(String... strings) {
        userService.saveUser(new User("Rodrigo Suco"));
    }

}