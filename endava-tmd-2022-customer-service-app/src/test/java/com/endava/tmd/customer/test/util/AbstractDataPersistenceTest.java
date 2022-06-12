package com.endava.tmd.customer.test.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.endava.tmd.customer.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@IntegrationWebTest
public abstract class AbstractDataPersistenceTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("ownerDataSource")
    private DataSource ownerDataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void loadTestData() {
        runSQLScript("db/automatedTests/addCustomerData.sql");
    }

    @AfterEach
    public void clearTestData() {
        // Only the owner of the sequence can restart it
        runSQLScriptAsDbOwner("db/automatedTests/clearTables.sql");
    }

    protected void runSQLScript(final String... resourceLocations) {
        doRunSQLScript(dataSource, resourceLocations);
    }

    private void runSQLScriptAsDbOwner(final String... resourceLocations) {
        doRunSQLScript(ownerDataSource, resourceLocations);
    }

    private void doRunSQLScript(final DataSource ds, final String... resourceLocations) {
        final var populator = new ResourceDatabasePopulator();
        for (final var location : resourceLocations) {
            populator.addScript(new ClassPathResource(location));
        }
        DatabasePopulatorUtils.execute(populator, ds);
    }

    protected Customer getCustomerEntity(final Long id) {
        final RowMapper<Customer> rowMapper = (rs, rowNum) -> getClaimsMetadataEntity(rs);
        return getEntityById("customer", id, rowMapper);
    }

    private <T> T getEntityById(final String tableName, final Long id, final RowMapper<T> rowMapper) {
        final var sql = "SELECT * FROM customer_service_it." + tableName + " WHERE id = ?";
        return oneEntity(jdbcTemplate.query(sql, rowMapper, id));
    }

    private <T> T oneEntity(final List<T> entities) {
        return entities.isEmpty() ? null : entities.get(0);
    }

    private Customer getClaimsMetadataEntity(final ResultSet rs) throws SQLException {
        final var customer = new Customer()
                .setId(getLong(rs, "id"))
                .setCreateDateTime(rs.getObject("create_date_time", OffsetDateTime.class))
                .setLastUpdateDateTime(rs.getObject("last_update_date_time", OffsetDateTime.class))
                .setFirstName(rs.getString("first_name"))
                .setLastName(rs.getString("last_name"))
                .setDateOfBirth(rs.getDate("date_of_birth").toLocalDate())
                .setSecurityQuestions(convertToMap(rs.getString("security_questions")));
        try {
            final var versionField = customer.getClass().getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(customer, rs.getLong("version"));
        } catch (ReflectiveOperationException | SecurityException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> convertToMap(final String json) {
        try {
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Long getLong(final ResultSet rs, final String columnName) throws SQLException {
        final var id = rs.getLong(columnName);
        return rs.wasNull() ? null : id;
    }

}
