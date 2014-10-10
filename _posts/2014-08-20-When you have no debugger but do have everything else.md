---
longtitle: When you have no debugger but do have everything else
categories: java_snippet
---

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
system under test, and clean up the fixtures. Inserting this snippet just before
the test framework tears down your fixtures allows you to save yourself the
manual work setting up the test data manually and exercising the system by hand
steps. and your test framework setting up the test data fixtures set up prior to
easily stop a test and explore your application without manually setting up
those fixtures:

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