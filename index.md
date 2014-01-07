---
layout: index
title: Tricks for the CLI by yegeniy
name: Tricks for the CLI
tagline: Mostly bash, os x, java
---



2014-01-02
----------

##### [`git`: commands I commonly use, relevant ramblings, and configuration.](https://www.readability.com/articles/kqmddftx)

A Readability-wrapped version of https://gist.github.com/yegeniy/1125520

2013-12-20
----------

##### Switch between different versions of maven using `homebrew`

Assuming you have brew's `maven v3.1.1` installed, leverage the `brew versions` command to install maven v3.0.5. 

```bash
$ brew unlink maven
$ brew versions maven
$ cd `brew --prefix`
# b4725ca happens to be the SHA for maven v3.0.5
$ git checkout b4725ca Library/Formula/maven.rb
$ brew install maven
```

Then, you can switch between both versions at will with `brew switch`.

```bash
$ brew switch maven 3.1.1
$ brew switch maven 3.0.5
```

> _Adapted from http://stackoverflow.com/a/4158763/2916086_

2013-12-18
----------

##### Format OS X Clipboard contents as JSON

```bash
pbpaste|python -m json.tool
```

2013-12-04
----------

##### [Article called 'Week Of Unix Tools'](http://www.semicomplete.com/articles/week-of-unix-tools/).

It'll kind of teach you to list what it will teach you:

```bash
$ echo 'Day 1: sed
Day 2: cut and paste
Day 3: awk
Day 4: data source tools
Day 5: xargs' | cut -d' ' -f 3- | paste -s -d, - | sed -e 's/,/, /g'
sed, cut and paste, awk, data source tools, xargs
```


2013-12-03
----------

##### Maven Dependency Analysis

    mvn dependency:analyze | less

[It](http://maven.apache.org/plugins/maven-dependency-plugin/) will show you `unused declared dependencies`, and `used undeclared dependencies` so you can add or remove dependencies as appropriate. Just don't go crazy with it.

Update: (mis?)Using this introduced some problems into my code because I removed dependencies whose transitive dependencies were being used.

2013-10-31
----------

##### Running a Debugger on an Application running in Tomcat using JPDA

Start Tomcat with `catalina jpda start`. It will start Tomcat so that a remote debugger can be connected to port 8000.

Set up your debugger to run with the following options when the JVM is started:

    -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n

In IDEA, this done in the Debug Configurations by setting up the "Remote" configuration.

http://wiki.apache.org/tomcat/FAQ/Developing#Q1
http://stackoverflow.com/questions/11480563/debugging-with-tomcat-and-intellij-community-edition

2013-10-30
----------


##### Packaging a multi-module project with maven for the first time.

If you have internal dependencies (e.g. multi-module project), you should run `mvn install` _on that dependency_ before running `mvn package` on the multi-module project. At least the first time, to get the dependencies installed into your local repository (under `~/.m2/repository/`).

2013-10-22
----------

##### Fixing indendation in poms

    export XMLLINT_INDENT="   "; for filepath in `find . -name pom.xml |xargs`; do xmllint --format --output $filepath $filepath; done