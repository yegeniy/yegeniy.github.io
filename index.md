---
layout: index
title: Tricks for the CLI by yegeniy
name: Tricks for the CLI
tagline: Mostly bash, os x, java
---

2014-02-20
----------

##### Undo a release made by maven-release-plugin and git

This command is a heuristic so it doesn't really care about maven-release plugin, basically it's supposed to check out the latest master branch, delete the local and remote tags, and undo 2 commits. 
It assumes that no one has pushed any commits in since you made a release with [`maven-release-plugin`](http://maven.apache.org/maven-release/maven-release-plugin/examples/prepare-release.html).

```bash
function git-undo-mvn-release { TAG_NAME=$1 && echo "  >>> undoing last two commits and removing tag named '${TAG_NAME}' <<<" && echo "  getting and checking out the latest copy of the the master branch" && git fetch && git checkout master && git pull origin master && git status && echo "  removing local and remote tag ${TAG_NAME} if both exist" && git tag -d ${TAG_NAME} && git push origin :${TAG_NAME} && echo "  git reset HEAD^^ --hard" && git reset HEAD^^ --hard && git status && git log --oneline | head && echo '  >>> If you are satisfied, run `git push origin master --force` <<<'; }
```

Here is a sample of how it should work:

```bash
$ git log --oneline | head -n 1
b747131 a real commit... the one you want to get back to.

$ mvn release:prepare 
# ...

$ git log --oneline | head -n 3
db09eda [maven-release-plugin] prepare for next development iteration
00024fe [maven-release-plugin] prepare release test/1.0
b747131 a real commit... the one you want to get back to.

$ git-undo-mvn-release test/1.0
  >>> undoing last two commits and removing tag named 'test/1.0' <<<
  getting and checking out the latest copy of the the master branch
Already on 'master'
From https://github.com/yegeniy/yegeniy.github.io
 * branch            master     -> FETCH_HEAD
Already up-to-date.
On branch master
nothing to commit, working directory clean
git reset HEAD^^ --hard
HEAD is now at b747131 a real commit... the one you want to get back to.
  removing local and remote tag test/1.0 if both exist
Deleted tag 'test/1.0' (was db09eda)
To https://github.com/yegeniy/yegeniy.github.io.git
 - [deleted]         test/1.0
On branch master
nothing to commit, working directory clean
  >>> If you are satisfied, run `git push origin master --force` <<<

$ git log --oneline | head -n 1
b747131 a real commit... the one you want to get back to.

$ git push origin master # --force
```

2014-01-27
----------

##### Enable assertions on package tree in java

Easy to overlook, but you should include ellipses (`...`) after a package
name, to also enable assertions on it and all its subpackages.

```bash
$ java -? 2>&1 |grep -A2 -e '-ea'
    -ea[:<packagename>...|:<classname>]
    -enableassertions[:<packagename>...|:<classname>]
                  enable assertions with specified granularity
```

So, use `-ea:com.foo...` to enable assertions on `com.foo` and all its subpackages.


2014-01-25
----------

##### Exit from a running script using `Ctrl+C`

```bash
int_handler()
{
    echo "Interrupted."
    # Kill the parent process of the script.
    kill $PPID
    exit 1
}
trap 'int_handler' INT
```

Get more info on [`tr`ap](test.html) with:

```bash
$ help trap
```

Update: You can also add `set -e` to the top of your script to "Exit immediately
if a [subsequent] command exits with a non-zero status." That way if you won't
have to rely on being there to hit `Ctrl+C` when something goes wrong and you
won't need to rely on `$PPID` being set in `int_handler()` either.

2014-01-08
----------

##### Collate multiple log files, chronologically

Set up your logging so that your log statements start with a very simple [datePattern](http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html), such as `yyyyMMddHHmmssSSS`. Then, you should be able to see a collated view using:

```bash
$ ls *.log | xargs sort -bsnm | less -S
```

Even log statements spanning multiple lines will be sorted together. For example, when logging an error's stacktrace, only the first line has the necessary datePattern. The above command should keep most of those multi-line statements together and sort them by using the datePattern of the first line.

2014-01-02
----------

##### [`git`: commands I commonly use, relevant ramblings, and configuration.](https://gist.github.com/yegeniy/1125520)

https://gist.github.com/yegeniy/1125520

2013-12-20
----------

##### Switch between different versions of maven using `homebrew`'s deprecated `versions` command

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

_UPDATE_: As the updated title now suggests, `versions` is deprecated and will soon be removed. You can try to do this with `brew tap homebrew/versions && brew search maven` and ...

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