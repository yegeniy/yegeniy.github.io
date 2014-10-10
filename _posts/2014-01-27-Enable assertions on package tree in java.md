---
categories: cli java_snippet
---

Easy to overlook, but you should include ellipses (`...`) after a package
name, to also enable assertions on it and all its subpackages.

```bash
$ java -? 2>&1 |grep -A2 -e '-ea'
    -ea[:<packagename>...|:<classname>]
    -enableassertions[:<packagename>...|:<classname>]
                  enable assertions with specified granularity
```

So, use `-ea:com.foo...` to enable assertions on `com.foo` and all its subpackages.