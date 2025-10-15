package io.github.codicis.asn1.extension

import io.github.codicis.asn1.model.Asn1TaskConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class DefaultAsn1Extension @Inject constructor(
    private val objects: ObjectFactory
) : Asn1Extension {

    // Global version property
    override val version: Property<String> = objects.property(String::class.java).convention("1.14.0")

    override val tasks: NamedDomainObjectContainer<Asn1TaskConfig> =
        objects.domainObjectContainer(Asn1TaskConfig::class.java)
}