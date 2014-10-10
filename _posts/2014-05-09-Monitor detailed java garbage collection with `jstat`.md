---
longtitle: Monitor detailed java garbage collection with `jstat`
categories: cli java_snippet
---

```bash
$ /usr/java/jdk1.7.0_10/bin/jstat -gc -t -h 20 <PID> 5s | tee ~/jstat.out
```

The meaning of the column headers is detailed in <http://docs.oracle.com/javase/7/docs/technotes/tools/share/jstat.html#gc_option>.

Also interesting to note is that, by default, Java 7 seems to shrink the Eden
space over time; at least, when it can do so. This causes an increase in the
rate of the incremental garbage collection; however, this is nothing to be
alarmed at as long as full garbage collection rate does not increase.