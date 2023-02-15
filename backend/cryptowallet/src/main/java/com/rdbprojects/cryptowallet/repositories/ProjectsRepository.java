package com.rdbprojects.cryptowallet.repositories;

import com.rdbprojects.cryptowallet.entities.Projects;
import com.rdbprojects.cryptowallet.entities.Users;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface ProjectsRepository extends CassandraRepository<Projects, Integer> {
    @Query("select * from projects where projectname = ?0 ALLOW FILTERING")
    public Projects findProjectByName(String projectName);
}
