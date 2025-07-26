package io.github.codicis.asn1

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class Asn1CompilerPluginTest {
    val packageName = "com.acme.asn1"
    val packageDir = packageName.replace('.', '/')
    val outputDirectory = "generated/asn1"

    @TempDir
    lateinit var testProjectDir: File

    private fun writeBuildScript() {
        val buildFile = File(testProjectDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("java")
                id("io.github.codicis.asn1")
            }
            repositories {
                mavenCentral()
            }
            asn1 {
                version.set("1.14.0")
                packageName.set("$packageName")
                sourceFiles.setFrom(fileTree("src/main/asn1") { include("**/*.asn1") })
                compilerClasspath.setFrom(configurations.getByName("asn1Compiler"))
                outputDirectory.set(layout.buildDirectory.dir("$outputDirectory"))
            }
            """.trimIndent()
        )
    }

    private fun writeSettingsFile() {
        File(testProjectDir, "settings.gradle.kts").writeText("")
    }

    private fun writeAsn1File() {
        File(testProjectDir, "src/main/asn1/").mkdirs()
        File(testProjectDir, "src/main/asn1/example.asn1")
            .writeText(
                """
                UserProfile DEFINITIONS ::= BEGIN

                User ::= SEQUENCE {
                    id            INTEGER,
                    name          UTF8String,
                    email         IA5String OPTIONAL,
                    birthDate     GeneralizedTime OPTIONAL
                }

                END
            """.trimIndent()
            )
    }
    @Test
    fun `plugin applies correctly`() {
        writeSettingsFile()
        writeBuildScript()
        writeAsn1File()

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("tasks")
            .build()

        assertTrue(result.output.contains("asn1Compile"))
        assertEquals(TaskOutcome.SUCCESS, result.task(":tasks")?.outcome)
    }

    @Test
    fun `task executes`() {
        writeSettingsFile()
        writeBuildScript()
        writeAsn1File()

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("asn1Compile")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":asn1Compile")?.outcome)

        assertTrue(testProjectDir.resolve("build/$outputDirectory/$packageDir/userprofile/User.java").exists())
    }
}