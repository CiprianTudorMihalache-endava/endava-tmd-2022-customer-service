package com.endava.tmd.customer.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "customer")
@TypeDef(name = "jsonb", typeClass = JsonType.class)
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "create_date_time", updatable = false, nullable = false)
    private OffsetDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "last_update_date_time", nullable = false)
    private OffsetDateTime lastUpdateDateTime;

    @Version
    @Setter(AccessLevel.PRIVATE)
    private Long version;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Type(type = "jsonb")
    @Column(name = "security_questions", updatable = false, nullable = false, columnDefinition = "jsonb")
    // There is also the JPA standard persistence of a map, but it requires an additional database table
    // https://stackoverflow.com/a/3393780/2032694
    private Map<String, String> securityQuestions = new HashMap<>();
}
