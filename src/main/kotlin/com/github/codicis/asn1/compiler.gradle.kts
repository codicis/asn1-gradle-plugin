package com.github.codicis.asn1

import org.gradle.kotlin.dsl.invoke

plugins {
    id("java")
}

/**
 * This interface represents an extension object for the ASN1 compiler plugin.
 * It provides a way to configure the version of the ASN1 compiler used in the build.
 */
interface Asn1CompilerPluginExtension {

    /**
     * Property to hold the version of the ASN1 compiler.
     * The default value is set to "1.14.0".
     *
     * @return a {@link Property} of type {@link String} representing the version of the ASN1 compiler.
     */
    val version: Property<String>
}
// Add the 'greeting' extension object to project
val extension = project.extensions.create<Asn1CompilerPluginExtension>("asn1")

// Set a default value for 'message'
extension.version.convention("1.14.0")

val asn1bean: Configuration by configurations.creating {
    extendsFrom(configurations.compileClasspath.get())
}

dependencies {
    asn1bean("com.beanit:asn1bean-compiler:${extension.version.get()}")
}


/**
 * This class represents a custom Gradle task for compiling ASN.1 definitions into Java classes.
 * It extends the JavaExec class and provides configuration options for the ASN.1 compiler.
 */
abstract class Asn1CompileTask : JavaExec() {

    /**
     * Property representing the package name for the generated Java classes.
     * This property is marked as an input for Gradle up-to-date checks.
     */
    @get:Input
    abstract val packageName: Property<String>

    /**
     * Property representing the input ASN.1 file to be compiled.
     * This property is marked as an input file for Gradle up-to-date checks.
     */
    @get:InputFile
    abstract val inputFile: RegularFileProperty

    /**
     * Property representing the output directory for the generated Java classes.
     * The default value is set to "build/generated/sources/{taskName}/main/java".
     * This property is marked as an output directory for Gradle up-to-date checks.
     */
    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("generated/sources/${name}/main/java"))

    /**
     * Initializes the task with static configuration: inputs and outputs.
     */
    init {
        group = "build"
        description = "The compiler reads the ASN.1 definitions from the given files and " +
                "generates corresponding Java classes that can be used to conveniently encode and decode BER data."
        mainClass = "com.beanit.asn1bean.compiler.Compiler"
    }

    /**
     * Overrides the exec method to set the arguments for the ASN.1 compiler.
     * This method is responsible for preparing the command-line arguments for the ASN.1 compiler.
     * It retrieves the input file, output directory, package name, and other necessary parameters
     * and constructs a list of arguments to be passed to the compiler.
     *
     * @see JavaExec#exec()
     */
    override fun exec() {
        // Construct a list of command-line arguments for the ASN.1 compiler
        args = listOf(
            "-f",
            inputFile.get().toString(),
            "-o", outputDirectory.get().toString(), // Output directory for generated Java classes
            "-p",
            packageName.get(),
            "-e"
        )
        super.exec()
    }
}

val asn1Compile = tasks.register<Asn1CompileTask>("asn1Compile") {
    classpath = asn1bean
}

sourceSets {
    main {
        java {
            srcDir("build/generated/sources/${asn1Compile.name}/main/java")
        }
    }
    test {
        java {
            srcDir("build/generated/sources/${asn1Compile.name}/main/java")
        }
    }
}