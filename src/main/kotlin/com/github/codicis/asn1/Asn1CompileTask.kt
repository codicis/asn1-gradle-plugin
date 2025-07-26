package com.github.codicis.asn1

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory

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
    @get:InputFiles
    abstract val files: ListProperty<RegularFile>

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
        this.mainClass.set("com.beanit.asn1bean.compiler.Compiler")
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
        val params = mutableListOf<String>()
        params.add("-f")
        files.get().forEach { file ->
            params.add(file.toString())
        }
        params.addAll(
            listOf(
                "-o", outputDirectory.get().toString(), // Output directory for generated Java classes
                "-p", packageName.get(),
                "-e"
            )
        )
        args(params)
        super.exec()
    }
}