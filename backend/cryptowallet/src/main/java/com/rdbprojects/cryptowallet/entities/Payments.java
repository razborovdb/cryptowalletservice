package com.rdbprojects.cryptowallet.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payments {
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String id;

    @Column("payment")
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double payment;

    @Column("paymentdescription")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String paymentDescription;

    @Column("userid")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String userId;

    @Column("projectid")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String projectId;

    @Column("paymentdate")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private String paymentDate;
}
