package com.wallet.walletclient.repository;

import com.wallet.walletclient.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findById(final int userId);


}
