---
longtitle: Switch between different versions of maven using `homebrew`
categories: cli
---

You can try to do this with something like the following:

```bash
$ brew tap homebrew/versions 
$ brew search maven
$ brew install maven30
$ brew unlink maven
$ brew link maven30
```

**You can also do this using the deprecated `versions` command:**

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