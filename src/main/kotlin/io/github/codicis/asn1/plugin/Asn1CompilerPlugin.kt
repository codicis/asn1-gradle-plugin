package io.github.codicis.asn1.plugin


import io.github.codicis.asn1.extension.DefaultAsn1Extension
import io.github.codicis.asn1.model.ModelSet
import io.github.codicis.asn1.task.Asn1CompileTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.getByType

class Asn1CompilerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.withPlugin("java") {

            val extension = project.extensions.create(
                "asn1",
                DefaultAsn1Extension::class.java,
                project.objects
            )

            // Define custom configuration for the compiler
            val compilerConfiguration = project.configurations.create("asn1Compiler").apply {
                description = "Configuration for ASN.1 compiler dependencies"
                isCanBeResolved = true
                isCanBeConsumed = false
                extendsFrom(project.configurations.getByName("compileClasspath"))
            }

            // Lazily add dependency
            val versionProvider = extension.version
            val depNotationProvider = versionProvider.map { version ->
                "com.beanit:asn1bean-compiler:$version"
            }

            compilerConfiguration.defaultDependencies {
                add(project.dependencies.create(depNotationProvider.get()))
            }

            // Configure tasks for each declared ASN.1 config lazily
            extension.container().all {
                configureAsn1Task(project, this, compilerConfiguration)
            }
        }
    }

    private fun configureAsn1Task(
        project: Project,
        model: ModelSet,
        compilerConfiguration: Configuration
    ) {
        val taskProvider = project.tasks.register(model.name, Asn1CompileTask::class.java).apply {
            configure {
                description = "Compile ASN.1 sources for '${model.name}'"
                group = "asn1"

                // Map extension model to task's nested config
                sourceFiles.setFrom(model.sourceFiles)
                outputDir.set(model.outputDir)
                packageName.set(model.packageName)

                compilerClasspath.from(compilerConfiguration)
            }
        }

        project.tasks.named("compileJava").configure {
            dependsOn(taskProvider)
            project.extensions.getByType<JavaPluginExtension>()
                .sourceSets.named("main") {
                    // Register generated sources directory, wired to the task output
                    java.srcDir(taskProvider.flatMap { it.outputDir })
                }
        }
    }
}