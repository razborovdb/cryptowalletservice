package com.rdbprojects.cryptowallet.entities;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.UUID;


@Table(value = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {
    @PrimaryKeyColumn(name = "email", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String email;

    @Column("name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String name;

    @Column("avatar")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String avatar;

    @Column("avatarurl")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String avatarUrl;

    @Column("password")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String password;

    @Column("role")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String role;

    @Column("isadmin")
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private Boolean isAdmin;

    @Override
    public String toString() {
        return "Users{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
