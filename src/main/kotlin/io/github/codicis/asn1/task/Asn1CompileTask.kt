package io.github.codicis.asn1.task

import io.github.codicis.asn1.model.Asn1TaskConfig
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

/**
 * Task that compiles ASN.1 definitions into Java sources.
 * Simplified to reuse Asn1TaskConfig for inputs/outputs via a @Nested bean.
 */
abstract class Asn1CompileTask : DefaultTask() {

    // Reuse the model bean for task inputs/outputs
    @get:Nested
    val config: Asn1TaskConfig = project.objects.newInstance(Asn1TaskConfig::class.java, name, project.objects).apply {
        // Provide a sensible default for the output directory, still overridable from the plugin DSL
        outputDirectory.convention(project.layout.buildDirectory.dir("generated/sources/${name}/main/java"))
    }

    @get:Inject
    abstract val execOperations: ExecOperations

    @get:Classpath
    abstract val compilerClasspath: ConfigurableFileCollection

    init {
        group = "build"
        description = "The compiler reads the ASN.1 definitions from the given files and generates Java classes."
    }

    @TaskAction
    fun compile() {
        val outputDir = config.outputDirectory.get().asFile
        outputDir.mkdirs()

        val asn1Files = config.sourceFiles.files
        if (asn1Files.isEmpty()) {
            logger.warn("No ASN.1 files found to compile.")
            return
        }
        logger.lifecycle("Compiling ASN.1 files: ${asn1Files.joinToString { it.name }}")

        val argsList = mutableListOf(
            "-o", outputDir.absolutePath,
            "-p", config.packageName.get(),
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