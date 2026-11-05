plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "2.2.1"
}
repositories {
    mavenCentral()
}



dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    website.set("https://github.com/codicis/asn1-gradle-plugin")
    vcsUrl.set("https://github.com/codicis/asn1-gradle-plugin")
    plugins {
        create("io.github.codicis.asn1") {
            implementationClass = "io.github.codicis.asn1.plugin.Asn1CompilerPlugin"
            displayName = "Gradle ASN1 compiler plugin"
            description = "This plugin provides a task for compiling ASN.1 definitions into Java classes."
            tags.set(listOf("asn1", "compiler", "java"))
        }
    }
}