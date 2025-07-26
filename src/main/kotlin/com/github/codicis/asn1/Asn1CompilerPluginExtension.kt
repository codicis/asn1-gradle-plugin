package com.github.codicis.asn1

import org.gradle.api.provider.Property

/**
 * This interface represents an extension object for the ASN1 compiler plugin.
 * It provides a way to configure the version of the ASN1 compiler used in the build.
 */
interface Asn1CompilerPluginExtension {

    /**
     * Property to hold the version of the ASN1 compiler.
     * The default value is set to "1.14.0".
     *
     * @return a {@link Property} of type {@link String} representing the version of the ASN1 compiler.
     */
    val version: Property<String>
}