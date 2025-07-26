package com.github.codicis.asn1

import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.creating

class Asn1CompilerPlugin : org.gradle.api.Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.withPlugin("java") {

            val extension = target.extensions.create<Asn1CompilerPluginExtension>("asn1")

            target.tasks.register("compileAsn1", Asn1CompileTask::class.java) {
                group = "build"
                description = "Compile ASN.1 definitions into Java classes"
            }

            target.configurations.creating {
                extendsFrom(target.configurations.getByName("compileClasspath"))
            }
            target.dependencies.add("asn1bean", "com.beanit:asn1bean-compiler:${extension.version.get()}")
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