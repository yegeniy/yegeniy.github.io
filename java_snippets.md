---
layout: index
title: Java Snippets by yegeniy
name: Java Snippets
tagline: Stuff that's hard to look up<br/>Candidates for utility classes
---

<a name="2014-08-20" href="#2014-08-20">2014-08-20</a>
----------

##### When you have no debugger but do have everything else.

Consider inserting one of the following snippets in testing code to simulate
breakpoints:

* Stop until you manually kill the test (e.g. `Ctrl-C`):

```java
synchronized(this) { wait(); }
```

* Pause until you resume the test (i.e. press any key to continue):

```java
try { System.in.read(); } catch (Throwable t) { System.out.println("Error while paused: " + t); }
```

---

This is useful if you need to pause test execution half-way through, and have
made up a good reason to avoid setting up breakpoints within a debugger.

This approach is not as powerful as a decent debugger's breakpoint would be, but
it's still useful; particularly when placed within a test that is executed
separately from your application. You end up pausing the test abruptly, so a
single- threaded test framework (JUnit's default) won't get a chance to run your
clean up methods until you resume.

Usually a test will do the following: set up fixtures (test data), exercise the
system under test, and clean up the fixtures. Inserting this snippet just before the test framework tears down your fixtures allows you to save yourself the manual work setting up the test data manually and exercising the system by hand steps. and your test framework setting up the test data fixtures set up prior to easily stop a test and explore your application without manually setting up those fixtures:

```java
synchronized(this) { wait(); }
```

This should be a decent start but you could add more functionality. You could
even embed a REPL into your test to analyze your application state via a CLI. As
a simple example, the ability to resume test execution can be added so that the
test framework will attempt to clean up any test fixtures it set up.

A resumeable breakpoint can be implemented with something along the lines of:

```java
try { System.in.read(); } catch (Throwable t) { System.out.println("Error while paused: " + t); }
```

If you do this and you run your tests with Maven's Surefire Plugin (that's the
default for the `mvn test` command), you might run into a problem where your 
test does not listen to your input. As far as I understand, this is because
[Maven Surefire Plugin forks][mvntest] your test to a separate process that is 
not aware of input coming in from your terminal's `stdin`. The workaround is 
pretty simple once you know what to look for. Run your tests with the 
`-DforkCount=0` flag:

```
mvn -DforkCount=0 test
```

[mvntest]: http://maven.apache.org/surefire/maven-surefire-plugin/examples/debugging.html#Non-forked_Tests

<!--
<a name="2014-08-02" href="#2014-08-02">2014-08-02</a>
----------

##### Generate Boilerplate Classes via Annotation Processing at Compile Time

**This is a draft**

This topic is a little bigger than a snippet, due to the amount of wiring
required. Please refer to the external links for a more complete treatment.

Reference: <http://deors.wordpress.com/2011/10/08/annotation-processors/>

Use an annotation and an accompanying Annotation Processor, to generate Java
classes from a Mustache template at compile time.

1) Set up Maven Project for Annotation Processing

The most confusing part about this approach is setting up your environment to
kick off the annotation processing.

2) Create Annotation and Annotate your target Types

This is the step that you are most likely already familiar with. Your annotation is created using the `@interface` keyword.

3) Template the Generated class with jMustache.

4) Use Java's Annotation Processing Tools to Generate the Classes

The `javax.lang.model.*` packages are used to extract information about your annotated types (the class, method, etc.. that you annotated).
The `javax.annotation.processing` package is used to wire the Annotation and Annotated classes to the Annotation Processor.
The `javax.tools` package has some useful tooling. For example `JavaFileObject` generates the `.class` file, and `Diagnostic` can be useful for sending messages at compile-time.

pom.xml
annotations pom.xml
client pom.xml
processors pom.xml

annotations src main java com example MarkedForProcessing.java
client src main java com example MainClass.java
client src main java com example Thing.java
processors src main java com example MyAnnotationProcessor.java
processors src main resources META-INF services javax.annotation.processing.Processor
src main java com example Bindable.class

-->

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
