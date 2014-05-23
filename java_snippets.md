---
layout: index
title: Java Snippets by yegeniy
name: Java Snippets
tagline: Stuff that's hard to look up<br/>Candidates for utility classes
---

<a name="2014-05-23" href="#2014-05-23">2014-05-23</a>
----------

##### Generate MD5 checksums with `MessageDigest` and `BigInteger`

```java
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

private String asMD5Checksum(final String original) {
    final byte[] bytes = original.getBytes("UTF8");
    try {
        return new BigInteger(1,
            MessageDigest.getInstance("MD5").digest()
        ).toString(16);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
```

Consider leveraging `ThreadLocal<MessageDigest>::get()` to avoid instatiating
`MessageDigest` on every call:

```java
private static final ThreadLocal<MessageDigest> messageDigest = new ThreadLocal<MessageDigest>() {
    @Override
    protected MessageDigest initialValue() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
};
```

<a name="2014-05-12" href="#2014-05-12">2014-05-12</a>
----------

##### JAXB Namespaces and Package annotations with package-info.java

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

##### A Blocking Drain for `BlockingQueue`

```java
final BlockingQueue<Foo> outbound;
final Collection<Foo> outboundElements;

while(true) {
    outboundElements.add(outbound.take());
    outbound.drainTo(outboundElements);
    // Work with the contents of outboundElements for a while...
    outboundElements.clear();    
}
```

BlockingQueue::drainTo is not blocking, and BlockingQueue::take only takes a
single element. Putting the two together could be useful for applications that
have periods of high activity, low activity, and no activity at arbitrary times.
This should help keep the `outbound` queue from filling up too much during busy
periods, while preventing your application from going through the work step
repeatedly with zero elements.

Note that this isn't a very efficient algorithm, for when elements first start
coming in. So, if the work you need to do is so expensive that doing it twice
when you could do it once is prohibitive, consider exploring a more
sophisticated approach.

<a name="2014-03-07" href="#2014-03-07">2014-03-07</a>
----------

##### Keep (main) thread alive using [`Thread::join`](http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join\(\))

This is _a_ way of keeping the main thread (or any other one really) alive. Not
always the right approach though.

```java
final ExecutorService stayAlive = Executors.newSingleThreadExecutor();
stayAlive.submit(new Runnable() {
    @Override
    public void run() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
        } finally {
            LOG.debug("resubmitting stayAlive runnable");
            stayAlive.submit(this);
        }
    }
});
```

##### Shutdown and Await Termination of [`ExecutorService`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html)

```java
// From ExecutorService's JavaDoc
private static void shutdownAndAwaitTermination(final ExecutorService pool) {
    pool.shutdown(); // Disable new tasks from being submitted
    try {
        // Wait a while for existing tasks to terminate
        if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            pool.shutdownNow(); // Cancel currently executing tasks
            // Wait a while for tasks to respond to being cancelled
            if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                System.err.println("pool did not terminate");
        }
    } catch (InterruptedException ie) {
        // (Re-)Cancel if current thread also interrupted
        pool.shutdownNow();
        // Preserve interrupt status
        Thread.currentThread().interrupt();
    }
}
```

Consider using [`LOG::error`](http://www.slf4j.org/api/org/slf4j/Logger.html#error\(java.lang.String, java.lang.Object...\)) instead of `System.err::println`. 

Where `LOG` is:

```java
private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(CurrentClass.class);
```
