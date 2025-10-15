package io.github.codicis.asn1.model

import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Internal
import javax.inject.Inject

abstract class Asn1TaskConfig @Inject constructor(
    private val name: String,
    objects: ObjectFactory
): Named {

    @Internal
    override fun getName(): String = name

    /** ASN.1 input files to compile. */
    @get:InputFiles
    val sourceFiles: ConfigurableFileCollection = objects.fileCollection()

    /** The output directory for generated Java files. */
    @get:OutputDirectory
    val outputDirectory: DirectoryProperty = objects.directoryProperty()

    /** Java package name for generated classes. */
    @get:Input
    val packageName: Property<String> = objects.property(String::class.java)

}