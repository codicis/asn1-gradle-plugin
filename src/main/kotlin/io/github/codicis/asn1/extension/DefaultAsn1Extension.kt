package io.github.codicis.asn1.extension

import io.github.codicis.asn1.model.ModelSet
import io.github.codicis.asn1.model.ModelSetContainer
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class DefaultAsn1Extension @Inject constructor(
    objects: ObjectFactory
) : Asn1Extension {

    override val version: Property<String> = objects.property(String::class.java).convention("1.14.0")

    private val modelContainer: ModelSetContainer = ModelSetContainer(objects)

    override fun model(action: Action<NamedDomainObjectContainer<ModelSet>>) = modelContainer.configure(action)

    internal fun container(): NamedDomainObjectContainer<ModelSet> = modelContainer.container
}