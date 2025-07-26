package com.github.codicis.asn1

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import javax.inject.Inject

/**
 * This class represents a custom Gradle task for compiling ASN.1 definitions into Java classes.
 * It extends the JavaExec class and provides configuration options for the ASN.1 compiler.
 */
abstract class Asn1CompileTask : DefaultTask() {

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
    abstract val sourceFiles: ConfigurableFileCollection

    /**
     * Property representing the output directory for the generated Java classes.
     * The default value is set to "build/generated/sources/{taskName}/main/java".
     * This property is marked as an output directory for Gradle up-to-date checks.
     */
    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = project.objects.directoryProperty()
        .convention(project.layout.buildDirectory.dir("generated/sources/${name}/main/java"))

    @get:Inject
    abstract val execOperations: ExecOperations

    @get:Classpath
    abstract val compilerClasspath: ConfigurableFileCollection

    /**
     * Initializes the task with static configuration: inputs and outputs.
     */
    init {
        group = "build"
        description = "The compiler reads the ASN.1 definitions from the given files and " +
                "generates corresponding Java classes that can be used to conveniently encode and decode BER data."
    }

    @TaskAction
    fun compile() {
        val outputDir = outputDirectory.get().asFile
        outputDir.mkdirs()

        sourceFiles.files.forEach { asn1File ->
            logger.lifecycle("Compiling ASN.1 file: ${asn1File.name}")
            execOperations.javaexec {
                classpath = compilerClasspath
                mainClass.set("com.beanit.asn1bean.compiler.Compiler")
                args("-o", outputDir.absolutePath,
                    "-p", packageName.get(),
                    "-e",
                    "-f", asn1File.absolutePath
                )
            }
        }
    }
}