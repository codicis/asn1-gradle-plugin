plugins {
    `kotlin-dsl`
    `maven-publish`    
}
repositories {
    mavenCentral()
}

group = "com.github.codicis"
version = "0.2"

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