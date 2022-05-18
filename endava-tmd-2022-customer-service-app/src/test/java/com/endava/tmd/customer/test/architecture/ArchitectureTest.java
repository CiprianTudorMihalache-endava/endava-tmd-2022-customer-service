package com.endava.tmd.customer.test.architecture;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

// https://reflectoring.io/spring-hexagonal
// https://www.archunit.org/userguide/html/000_Index.html#_architectures
// real-life story: https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749

// Be careful that this may delay spring integration tests by ~1 sec: 
// https://github.com/TNG/ArchUnit/issues/546#issuecomment-808751137
// version 0.24 might fix the issue

@AnalyzeClasses(packages = "com.endava.tmd.customer", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    void testOnionArchitectureRespected(final JavaClasses javaClasses) {
        onionArchitecture()
                .domainModels("com.endava.tmd.customer.model..")
                .applicationServices("com.endava.tmd.customer.core..")
                .adapter("controller", "..adapter.in.web..")
                .adapter("database", "..adapter.out.db..")
                .withOptionalLayers(true) // Otherwise onionArchitecture() requires a domainServices() layer
                .check(javaClasses);
    }
}
