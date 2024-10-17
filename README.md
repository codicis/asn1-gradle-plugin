# ASN.1 Gradle Compiler Plugin
Gradle plugin base on Java ASN.1 BER and DER encoding/decoding

Using the plugins DSL:
```
plugins {
  id("com.github.codicis.asn1.compiler") version "0.1"
}
````

Configure the plugin using the "asn1" extension like this:
````
asn1 {
    // Set asn1bean compiler version properties here...
    version = "1.14.0"
}
````


### Additional Links
For further reference, please consider the following sections:
* [ASN1bean Overview](https://www.beanit.com/asn1/)
* [ASN1bean on GitHub](https://github.com/beanit/asn1bean)