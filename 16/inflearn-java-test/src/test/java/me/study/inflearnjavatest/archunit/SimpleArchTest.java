package me.study.inflearnjavatest.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class SimpleArchTest {
    @Test
    void domainPackageDependencyTests() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("me.study.inflearnjavatest");
        ArchRule myRule = classes().that().resideInAPackage("..domain..")
                .should().onlyBeAccessed().byClassesThat()
                .resideInAnyPackage("..study..", "..member..", "..domain..");
        myRule.check(importedClasses);
    }
}
