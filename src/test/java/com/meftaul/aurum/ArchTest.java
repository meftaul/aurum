package com.meftaul.aurum;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.meftaul.aurum");

        noClasses()
            .that()
                .resideInAnyPackage("com.meftaul.aurum.service..")
            .or()
                .resideInAnyPackage("com.meftaul.aurum.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.meftaul.aurum.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
