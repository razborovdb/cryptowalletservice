package com.rdbprojects.cryptowallet.repositories;

import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.entities.Wallets;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface WalletsRepository extends CassandraRepository<Wallets, Integer> {

    @Query("select * from wallets where userid = ?0 and walletname = ?1 ALLOW FILTERING")
    public Wallets findWalletByWalletNameAndUserId(String userId, String walletName);
    @Query("select * from wallets where userid = ?0 ALLOW FILTERING")
    public List<Wallets> findWalletsByUserId(String userId);
}
