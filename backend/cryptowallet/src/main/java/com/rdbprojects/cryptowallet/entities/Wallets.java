package com.rdbprojects.cryptowallet.entities;

import com.rdbprojects.cryptowallet.udt.CryptoCurrenciesModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table(value = "wallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wallets {
    @PrimaryKeyColumn(name = "userid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String userId;

    @PrimaryKeyColumn(name = "walletname", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String walletName;

    @Column("walletdescription")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String walletDescription;

    @Column("cryptoscount")
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double cryptosCount;

    @Column("cryptoscost")
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double cryptosCost;

    @Column("cryptocurrencieslist")
    @CassandraType(userTypeName = "cryptocurrenciesmodel", type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UDT)
    private List<CryptoCurrenciesModel> cryptocurrenciesList;

    @Override
    public String toString() {
        return "Wallets{" +
                "userId='" + userId + '\'' +
                ", walletName='" + walletName + '\'' +
                ", walletDescription='" + walletDescription + '\'' +
                ", cryptosCount=" + cryptosCount +
                ", cryptosCost=" + cryptosCost +
                ", cryptocurrenciesList=" + cryptocurrenciesList +
                '}';
    }
}
