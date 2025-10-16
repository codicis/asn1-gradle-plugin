package io.github.codicis.asn1.model

import org.gradle.api.Named
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

interface ModelSet : Named {
    val sourceFiles: ConfigurableFileCollection
    val packageName: Property<String>
    val outputDir: DirectoryProperty
}