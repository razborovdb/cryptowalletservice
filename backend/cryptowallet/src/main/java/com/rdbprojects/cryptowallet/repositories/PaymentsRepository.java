package com.rdbprojects.cryptowallet.repositories;

import com.rdbprojects.cryptowallet.entities.Payments;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface PaymentsRepository extends CassandraRepository<Payments, Integer> {
    @Query("select * from payments where id = ?0 ALLOW FILTERING")
    public Payments findPaymentById(String id);

    @Query("select * from payments where userid = ?0 ALLOW FILTERING")
    public List<Payments> findPaymentByUserId(String userId);
}
