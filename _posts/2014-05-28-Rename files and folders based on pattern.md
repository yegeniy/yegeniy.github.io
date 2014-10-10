---
category: cli
---

This loop will change the first substring `old` to `new` for any matching files
or folders.

```bash
for i in `find . -path ./skipme -prune -o -name "*old*"`; do
    newname=`echo $i | sed 's/old/new/g'`
    echo $i "->" $newname
    mv $i $newname
done
```

You will need to run it more than once if any of the matching files or folders
have more than one substring matching `old` (e.g. `my/old/directory/is/old.txt`).

The `path ./skipme -prune -o` bit means don't look in the `skipme` directory.
This isn't perfect, since the `skipme` directory itself could be renamed if
`-name`'s pattern matched it, but this loop should work well enough for most
cases.