package io.github.codicis.asn1.model

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject


abstract class DefaultModelSet @Inject constructor(
    private val name: String,
    objects: ObjectFactory,
) : ModelSet {
    override fun getName(): String = name

    override val sourceFiles: ConfigurableFileCollection = objects.fileCollection()
    override val packageName: Property<String> = objects.property(String::class.java)

    // Optional DSL helper if you want `sourceFiles { ... }` style
    fun sourceFiles(configure: ConfigurableFileCollection.() -> Unit) = sourceFiles.configure()
}
