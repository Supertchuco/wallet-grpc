package com.wallet.walletserver.service;

import com.wallet.walletserver.entity.User;
import com.wallet.walletserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserById(final int id){
        return userRepository.findById(id);
    }

    public void validateUser(final User user){
        if(Objects.isNull(user)){
            log.info("user not found");

        }
    }

}
