---
longtitle: Maven Dependency Analysis
categories: cli java_snippet
---

    mvn dependency:analyze | less

[It](http://maven.apache.org/plugins/maven-dependency-plugin/) will show you `unused declared dependencies`, and `used undeclared dependencies` so you can add or remove dependencies as appropriate. Just don't go crazy with it.

Update: (mis?)Using this introduced some problems into my code because I removed dependencies whose transitive dependencies were being used.
2014/10/10 Update: The thing to pay attention to really is the `used undeclared dependencies`. They list the landmines where you are using a transitive dependency directly.