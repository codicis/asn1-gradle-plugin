package io.github.codicis.asn1.task


import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import javax.inject.Inject

/**
 * Task that compiles ASN.1 definitions into Java sources.
 * Simplified to reuse Asn1TaskConfig for inputs/outputs via a @Nested bean.
 */
abstract class Asn1CompileTask : DefaultTask() {

    @get:Input
    abstract val packageName: Property<String>


    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sourceFiles: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Inject
    abstract val execOperations: ExecOperations

    @get:Classpath
    abstract val compilerClasspath: ConfigurableFileCollection

    init {
        group = "asn1"
        description = "The compiler reads the ASN.1 definitions from the given files and generates Java classes."
    }

    @TaskAction
    fun compile() {
        val outputDir = outputDir.get().asFile
        outputDir.mkdirs()

        val asn1Files = sourceFiles.files
        if (asn1Files.isEmpty()) {
            logger.warn("No ASN.1 files found to compile.")
            return
        }
        logger.lifecycle("Compiling ASN.1 files: ${asn1Files.joinToString { it.name }}")

        val argsList = mutableListOf(
            "-o", outputDir.absolutePath,
            "-p", packageName.get(),
            "-e",
            "-f"
        ).apply {
            addAll(asn1Files.map { it.absolutePath })
        }

        execOperations.javaexec {
            classpath = compilerClasspath
            mainClass.set("com.beanit.asn1bean.compiler.Compiler")
            args(argsList)
        }
    }
}