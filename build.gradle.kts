plugins {
    `kotlin-dsl`
    `maven-publish`    
}
repositories {
    mavenCentral()
}

group = "com.github.codicis"
version = "0.2"

dependencies {
    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("asn1") {
            id = "com.github.codicis.asn1"
            implementationClass = "com.github.codicis.asn1.Asn1CompilerPlugin"
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