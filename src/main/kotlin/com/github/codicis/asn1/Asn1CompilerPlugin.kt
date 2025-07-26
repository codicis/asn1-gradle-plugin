package com.github.codicis.asn1

import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class Asn1CompilerPlugin : org.gradle.api.Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.withPlugin("java") {

            val extension = target.extensions.create<Asn1CompilerPluginExtension>("asn1")

            // Apply the default convention for the version property
            extension.version.convention("1.14.0")

            target.tasks.register("asn1Compile", Asn1CompileTask::class.java) {
                group = "build"
                description = "Compile ASN.1 definitions into Java classes"
            }

            // Register the custom configuration
            val asn1Configuration = target.configurations.create("asn1Compiler") {
                isVisible = true
                isTransitive = true
                description = "Configuration for ASN.1 compiler dependencies"

                // Inherit from compileClasspath
                extendsFrom(target.configurations.getByName("compileClasspath"))
            }

            target.dependencies.add("asn1Compiler", "com.beanit:asn1bean-compiler:${extension.version.get()}")
        }
        //sourceSets {
        //    main {
        //        java {
        //            srcDir("build/generated/sources/${asn1Compile.name}/main/java")
        //        }
        //    }
        //    test {
        //        java {
        //            srcDir("build/generated/sources/${asn1Compile.name}/main/java")
        //        }
        //    }
        //}
    }
}