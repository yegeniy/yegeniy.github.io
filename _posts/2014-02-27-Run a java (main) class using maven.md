---
category: cli java_snippet
---

[`exec-maven-plugin`](http://mojo.codehaus.org/exec-maven-plugin/java-mojo.html)
is a quick way to kick off Java scripts in a project that is already
using Maven.

```bash
mvn clean compile exec:java -Dexec.mainClass="canonical.name.of.class.Main"
```

Usually this class is going to live in the src/test directory instead of
src/main. In this case `exec:java` will need the `classpathScope="test"` as
well.

```bash
mvn clean compile exec:java -Dexec.mainClass="canonical.name.of.class.Main -Dexec.classpathScope="test"
```