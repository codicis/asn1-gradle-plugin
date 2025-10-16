package io.github.codicis.asn1.model

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

open class ModelSetContainer @Inject constructor(
    objects: ObjectFactory
) {

    // Create a container using the concrete implementation so Gradle can instantiate items
    @Suppress("UNCHECKED_CAST")
    val container: NamedDomainObjectContainer<ModelSet> =
        objects.domainObjectContainer(DefaultModelSet::class.java) as NamedDomainObjectContainer<ModelSet>

    fun configure(action: Action<NamedDomainObjectContainer<ModelSet>>) {
        action.execute(container)
    }
}
