package com.github.codicis.asn1

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Test
import java.io.File

class Asn1CompilerPluginTest {

    @TempDir
    lateinit var testProjectDir: File

    private fun writeBuildScript() {
        val buildFile = File(testProjectDir, "build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                id("java")
                id("com.github.codicis.asn1")
            }

            asn1 {
                version.set("1.14.0")
            }
            """.trimIndent()
        )
    }

    private fun writeSettingsFile() {
        File(testProjectDir, "settings.gradle.kts").writeText("")
    }

    @Test
    fun `plugin applies correctly`() {
        writeSettingsFile()
        writeBuildScript()

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("tasks")
            .build()

        assertTrue(result.output.contains("asn1Compile"))
        assertEquals(TaskOutcome.SUCCESS, result.task(":tasks")?.outcome)
    }
}