package com.rdbprojects.cryptowallet.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CryptoInWalletRequest {

    @Column("userid")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String userId;
    @Column("walletname")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String walletName;

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
        return "CryptoInWalletRequest{" +
                "userId='" + userId + '\'' +
                ", walletName='" + walletName + '\'' +
                ", cryptoName='" + cryptoName + '\'' +
                ", image='" + image + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", cryptoDescription='" + cryptoDescription + '\'' +
                ", cryptoAmount=" + cryptoAmount +
                ", cryptoCost=" + cryptoCost +
                '}';
    }
}