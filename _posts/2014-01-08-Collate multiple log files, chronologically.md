---
longtitle: Collate multiple log files, chronologically
categories: cli
---

Set up your logging so that your log statements start with a very simple [datePattern](http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html), such as `yyyyMMddHHmmssSSS`. Then, you should be able to see a collated view using:

```bash
$ ls *.log | xargs sort -bsnm | less -S
```

Even log statements spanning multiple lines will be sorted together. For example, when logging an error's stacktrace, only the first line has the necessary datePattern. The above command should keep most of those multi-line statements together and sort them by using the datePattern of the first line.