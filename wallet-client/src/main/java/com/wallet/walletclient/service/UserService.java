package com.wallet.walletclient.service;

import com.wallet.walletclient.entity.User;
import com.wallet.walletclient.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public void deleteUser(final User user) {
        userRepository.delete(user);
    }

    public User findUserById(final int id){
        return userRepository.findById(id);
    }
}
