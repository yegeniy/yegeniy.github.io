---
longtitle: JAXB Namespaces and Package annotations with package-info.java
categories: java_snippet
---

Package-level annotations apply to all classes in a package and live in
`package-info.java` under that package's directory.

Its specification is at [JLS 7.4.1](http://docs.oracle.com/javase/specs/jls/se7/html/jls-7.html#jls-7.4.1).

You can see an example of this in JAXB, which takes advantage of package-level
annotations in declaring the default namespace to use in a package. Actually, it
seems to be mandatory to declare a default namespace in `package-info.java` if
namespaces used as "markers", but are not backed by an XML schema. (Take that
last part with a grain of salt.)

* `com/foo/bar/package-info.java`:

```java
@javax.xml.bind.annotation.XmlSchema(namespace = "http://bar.foo.com/schema/1.0", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
package com.foo.bar;
```

You can then override the default namespace on sub elements within that package
by using the proper annotation:

```java
package com.foo.bar;

@XmlRootElement(namespace="")
public class Something { 
    @XmlRootElement
    public static class Subthing { }
}
```

Both `Something` and `Something.Subthing` will have an empty namespace, despite
the package-level namespace being `http://bar.foo.com/schema/1.0`
