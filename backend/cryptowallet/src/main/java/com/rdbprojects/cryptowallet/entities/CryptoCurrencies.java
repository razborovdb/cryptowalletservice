package com.rdbprojects.cryptowallet.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.Map;

@Table(value = "cryptocurrency")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CryptoCurrencies {
    @PrimaryKeyColumn(name = "cryptoname", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String cryptoName;

    @Column("image")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String image;

    @Column("imageurl")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String imageUrl;

    @Column("cryptodescription")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String cryptoDescription;

    @Column("cryptoamount")
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double cryptoAmount;

    @Column("cryptocost")
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double cryptoCost;


}
