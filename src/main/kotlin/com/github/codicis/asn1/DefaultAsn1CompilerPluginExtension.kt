package com.github.codicis.asn1

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class DefaultAsn1CompilerPluginExtension @Inject constructor(objects: ObjectFactory) :
    Asn1CompilerPluginExtension {

    override val version: Property<String> = objects.property(String::class.java).convention("1.14.0")
    override val packageName: Property<String> = objects.property(String::class.java)
    override val sourceFiles: ConfigurableFileCollection = objects.fileCollection()
    override val outputDirectory: DirectoryProperty = objects.directoryProperty()
    override val compilerClasspath: ConfigurableFileCollection = objects.fileCollection()
}