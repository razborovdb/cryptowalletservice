package com.rdbprojects.cryptowallet.repositories;

import com.rdbprojects.cryptowallet.entities.CryptoCurrencies;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface CryptoCurrenciesRepository extends CassandraRepository<CryptoCurrencies, Integer> {
    @Query("select * from cryptocurrency where cryptoname = ?0 ALLOW FILTERING")
    public CryptoCurrencies findCryptoByName(String name);
}
