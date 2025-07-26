package com.github.codicis.asn1

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.create

class Asn1CompilerPlugin : org.gradle.api.Plugin<Project> {

    override fun apply(project: Project) {
        project.pluginManager.withPlugin("java") {

            // Create the extension
            val extension = project.extensions.create<DefaultAsn1CompilerPluginExtension>("asn1")

            // Define custom configuration for the compiler
            val compilerConfiguration = project.configurations.create("asn1Compiler") {
                description = "Configuration for ASN.1 compiler dependencies"
                isCanBeResolved = true
                isCanBeConsumed = false
                extendsFrom(project.configurations.getByName("compileClasspath"))
            }

            // Add compiler dependency dynamically
            project.afterEvaluate {
                project.dependencies.add(
                    "asn1Compiler",
                    "com.beanit:asn1bean-compiler:${extension.version.get()}"
                )
            }

            // Register the compilation task
            val compileTask = project.tasks.register("asn1Compile", Asn1CompileTask::class.java) {
                group = "build"
                description = "Compile ASN.1 definitions into Java classes"

                packageName.set(extension.packageName)
                sourceFiles.setFrom(extension.sourceFiles)
                outputDirectory.set(extension.outputDirectory)
                compilerClasspath.setFrom(compilerConfiguration)
            }

            // Register generated sources and link them to the main source set
            project.afterEvaluate {
                val outputDir = compileTask.get().outputDirectory.get().asFile
                val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
                sourceSets.named("main") {
                    java.srcDir(outputDir)
                }
                project.tasks.named("compileJava") {
                    dependsOn(compileTask)
                }
            }
        }
    }
}