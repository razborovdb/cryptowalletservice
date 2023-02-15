package com.rdbprojects.cryptowallet.udt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.Map;

@UserDefinedType(value = "cryptocurrenciesmodel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CryptoCurrenciesModel {
    @Column("cryptoname")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String cryptoName;

    @Column("cryptotype")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String cryptoType;

    @Column("image")
    @CassandraType(type = CassandraType.Name.TEXT )
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

    @Override
    public String toString() {
        return "CryptoCurrenciesModel{" +
                "cryptoName='" + cryptoName + '\'' +
                ", image='" + image + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", cryptoDescription='" + cryptoDescription + '\'' +
                ", cryptoAmount=" + cryptoAmount +
                ", cryptoCost=" + cryptoCost +
                '}';
    }
}
