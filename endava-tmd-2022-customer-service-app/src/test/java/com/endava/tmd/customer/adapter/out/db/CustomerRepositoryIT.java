package com.endava.tmd.customer.adapter.out.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.endava.tmd.customer.model.Customer;
import com.endava.tmd.customer.test.util.AbstractDataPersistenceTest;
import com.endava.tmd.customer.test.util.TestConstants;
import com.endava.tmd.customer.test.util.mother.model.CustomerMother;

class CustomerRepositoryIT extends AbstractDataPersistenceTest {
    private static final String[] PERSISTENCE_FIELDS = {"id", "version", "createDateTime", "lastUpdateDateTime"};

    @Autowired
    private CustomerRepository repository;

    @Test
    void findByIdWhereExists() {
        final var customerShell = repository.findById(1L);
        assertThat(customerShell).isPresent().hasValueSatisfying(customer -> {
            final var expected = CustomerMother.jamesBond();
            assertThat(customer).usingRecursiveComparison().ignoringFieldsOfTypes(OffsetDateTime.class)
                    .isEqualTo(expected);
            assertThat(customer.getCreateDateTime()).isCloseTo(OffsetDateTime.now(), within(5, ChronoUnit.SECONDS));
            assertThat(customer.getLastUpdateDateTime()).isCloseTo(customer.getCreateDateTime(), within(1, ChronoUnit.MILLIS));
        });
    }

    @Test
    void findByIdWhereDoesNotExist() {
        assertThat(repository.findById(1234L)).isNotPresent();
    }

    @Test
    void saveObjectWithAllFields() {
        save(CustomerMother.peterPan());
    }

    @Test
    void saveObjectWithMandatoryFields() {
        save(CustomerMother.peterPan()
                .setLastName(null)
                .setSecurityQuestions(Map.of()));
    }

    @Test
    void saveObjectWithoutMandatoryAttributeThrowsException() {
        final var entity = CustomerMother.peterPan().setFirstName(null);
        assertThatThrownBy(() -> repository.saveAndFlush(entity))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("constraint [first_name]")
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

    private void save(final Customer originalEntity) {
        assertThat(originalEntity.getId()).isNull();
        assertThat(originalEntity.getCreateDateTime()).isNull();
        assertThat(originalEntity.getLastUpdateDateTime()).isNull();

        final var savedEntity = repository.saveAndFlush(originalEntity);

        assertThat(savedEntity.getId()).isEqualTo(TestConstants.INITIAL_DB_RECORDS + 1);
        assertThat(savedEntity.getCreateDateTime()).isCloseTo(OffsetDateTime.now(), within(5, ChronoUnit.SECONDS));
        assertThat(savedEntity.getLastUpdateDateTime()).isCloseTo(savedEntity.getCreateDateTime(), within(1, ChronoUnit.MILLIS));
        assertThat(savedEntity).usingRecursiveComparison().ignoringFields(PERSISTENCE_FIELDS)
                .isEqualTo(originalEntity);
        assertThat(savedEntity.getVersion()).isZero();

        // we will check also what is in DB using a JDBC connection to read record
        final var persistedEntity = getCustomerEntity(savedEntity.getId());
        assertThat(persistedEntity).usingRecursiveComparison().ignoringFields(PERSISTENCE_FIELDS)
                .isEqualTo(savedEntity);
        assertThat(persistedEntity.getCreateDateTime()).isCloseTo(savedEntity.getCreateDateTime(), within(1, ChronoUnit.MILLIS));
        assertThat(persistedEntity.getLastUpdateDateTime()).isCloseTo(savedEntity.getLastUpdateDateTime(),
                within(1, ChronoUnit.MILLIS));

        assertThat(persistedEntity.getVersion()).isZero();
    }

}
