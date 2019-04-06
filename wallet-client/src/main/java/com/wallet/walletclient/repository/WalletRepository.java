package com.wallet.walletclient.repository;

import com.wallet.walletclient.entity.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {

    @Query("SELECT wallet FROM Wallet wallet WHERE wallet.user.userId = :userId")
    List<Wallet> findWalletByUserId(final @Param("userId") int userId);
}