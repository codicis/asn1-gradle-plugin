# ASN.1 Gradle Compiler Plugin

This Gradle plugin provides easy integration of [ASN.1bean](https://github.com/beanit/asn1bean) — a Java-based library supporting **BER** and **DER** encoding/decoding of ASN.1 data structures.

## 📦 Plugin Setup

Add the plugin using the **plugins DSL** in your `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.codicis.asn1") version "0.1"
}
```

## ⚙️ Configuration

Configure the plugin via the `asn1` extension block:

```kotlin
asn1 {
    // Set the ASN.1 compiler version
    version = "1.14.0"

    // Define target package name for generated classes
    packageName.set("com.example.generated")

    // Provide input ASN.1 source files
    sourceFiles.setFrom(fileTree("src/main/asn1"))

    // Specify the compiler JAR path (Only if you really must owewrite default)
    compilerClasspath.setFrom(files("libs/asn1bean-compiler-${version.get()}.jar"))

    // Set the output directory for generated Java classes
    outputDirectory.set(layout.buildDirectory.dir("generated/asn1"))
}
```

## 📚 Resources

For more details and documentation, refer to:

- 🌐 [ASN.1bean Overview](https://www.beanit.com/asn1/)
- 🧩 [ASN.1bean GitHub Repository](https://github.com/beanit/asn1bean)