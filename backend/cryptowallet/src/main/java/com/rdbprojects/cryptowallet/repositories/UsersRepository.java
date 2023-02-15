package com.rdbprojects.cryptowallet.repositories;

import com.rdbprojects.cryptowallet.entities.Users;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface UsersRepository extends CassandraRepository<Users, Integer> {
    @Query("select * from users where email = ?0 ALLOW FILTERING")
    public Users findUserByEmail(String email);
}
