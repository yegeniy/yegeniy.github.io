---
longtitle: Dealing with used undeclared dependencies
categories: java_snippet
---

Used undeclared dependencies can be found with `mvn dependency:analyze`

1. `mvn dependency:analyze`
2. `mvn dependency:tree -Dincludes=groupId:artifactId:type:version`

How can I see where the code using an undeclared dependency is?

Exclude the dependency and see what fails.

_What's the issue?_

Potentially, your source code is using a library that your Project
Object Model (pom) does not declare as a dependency.

_How can I fix the issue?_

Declare your dependency explicitly.

_How can I avoid this issue?_

Only use classes that resolve as a first-level dependency. The easiest
way to do this is to be aware of the classes that should be available
to your source code. Avoid making your class choices by seeing what's
available on your IDE's autocomplete menu.

There is also "Transitive Dependency Management", but that's a bit heavy
handed. See more details in this nice overview:
<http://www.gradle.org/docs/current/userguide/dependency_management.html#sec:dependency_management_overview>
