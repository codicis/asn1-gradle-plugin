plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.3.1"
}
repositories {
    mavenCentral()
}

group = "io.github.codicis"
version = "0.1"

dependencies {
    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    website.set("https://github.com/codicis/asn1-gradle-plugin")
    vcsUrl.set("https://github.com/codicis/asn1-gradle-plugin")
    plugins {
        create("asn1") {
            id = "io.github.codicis.asn1"
            implementationClass = "io.github.codicis.asn1.Asn1CompilerPlugin"
            displayName = "Gradle ASN1 compiler plugin"
            description = "This plugin provides a task for compiling ASN.1 definitions into Java classes."
            tags.set(listOf("asn1", "compiler", "java"))
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/codicis/asn1-gradle-plugin")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }
}