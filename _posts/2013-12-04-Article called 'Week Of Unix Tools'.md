---
category: cli
---

<http://www.semicomplete.com/articles/week-of-unix-tools/>

It'll kind of teach you to list what it will teach you:

```bash
$ echo 'Day 1: sed
Day 2: cut and paste
Day 3: awk
Day 4: data source tools
Day 5: xargs' | cut -d' ' -f 3- | paste -s -d, - | sed -e 's/,/, /g'
sed, cut and paste, awk, data source tools, xargs
```