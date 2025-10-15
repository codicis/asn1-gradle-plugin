package io.github.codicis.asn1

import io.github.codicis.asn1.extension.DefaultAsn1Extension
import io.github.codicis.asn1.plugin.Asn1CompilerPlugin
import io.github.codicis.asn1.task.Asn1CompileTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class Asn1PluginUnitTest {
    @Test
    fun `plugin registers extension and tasks`() {
        // Create an in-memory Gradle project
        val project: Project = ProjectBuilder.builder().build()

        // Apply Java + our plugin
        project.pluginManager.apply(JavaPlugin::class.java)
        project.pluginManager.apply(Asn1CompilerPlugin::class.java)

        // Verify extension exists
        val ext = project.extensions.findByName("asn1")
        assertNotNull(ext, "asn1 extension should be registered")

        // Configure DSL programmatically
        val asn1Ext = ext as DefaultAsn1Extension
        asn1Ext.tasks.register("asn1Compile") {
            packageName.set("com.example.generated")
        }

        // Force task creation
        project.afterEvaluate {
            // Verify task is registered
            val task = project.tasks.findByName("asn1Compile")
            assertNotNull(task, "asn1Compile task should be created")
            assertTrue(task is Asn1CompileTask, "asn1Compile task should be created")

            // Verify compileJava depends on it
            val compileJava = project.tasks.getByName("compileJava")
            assertTrue(compileJava.taskDependencies.getDependencies(compileJava).contains(task))
        }

    }
}