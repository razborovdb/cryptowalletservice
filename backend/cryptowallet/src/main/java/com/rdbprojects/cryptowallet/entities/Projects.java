package com.rdbprojects.cryptowallet.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table(value = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Projects {
    @PrimaryKeyColumn(name = "projectname", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TEXT)
    private String projectName;

    @Column("image")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String image;

    @Column("imageurl")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String imageUrl;

    @Column("projectdescription")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String projectDescription;

    @Column("projectcost")
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double projectCost;
}
