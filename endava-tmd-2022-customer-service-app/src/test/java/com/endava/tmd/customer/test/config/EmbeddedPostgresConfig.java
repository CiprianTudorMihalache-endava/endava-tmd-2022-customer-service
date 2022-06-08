package com.endava.tmd.customer.test.config;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;

@Configuration
public class EmbeddedPostgresConfig {
    private static final String DB_NAME = "postgres";

    @Bean
    @Primary
    DataSource dataSource(@Value("${spring.liquibase.parameters.user_name}") final String userName) throws IOException {
        return embeddedPostgress().getDatabase(userName, DB_NAME);
    }

    @Bean
    @LiquibaseDataSource
    DataSource ownerDataSource(@Value("${spring.liquibase.parameters.owner_name}") final String ownerName)
            throws IOException, SQLException {
        final var db = embeddedPostgress().getPostgresDatabase();
        try (var connection = db.getConnection();
                var statement = connection.createStatement()) {
            statement.execute("CREATE ROLE " + ownerName + " WITH PASSWORD 'pass' LOGIN");
            statement.execute("ALTER USER " + ownerName + " WITH SUPERUSER");
        }
        return embeddedPostgress().getDatabase(ownerName, DB_NAME);
    }

    @Bean
    public EmbeddedPostgres embeddedPostgress() throws IOException {
        return EmbeddedPostgres.builder().start();
    }

}
