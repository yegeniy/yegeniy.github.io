---
longtitle: Rename files and folders based on pattern
categories: cli
---

This loop will echo commands that would change the first substring `old` to
`new` for any matching files or folders under your working directory.

```bash
for i in `find . -path ./skipme -prune -o -name "*old*"`; do
    newname=`echo $i | sed 's/old/new/g'`
    echo mv $i $newname
done
```

If you are happy with the result, remove the word `echo ` and rerun the loop so
that the `mv` command is executed instead of being echoed out. Note that this
loop only matches the first instance of `old` in any filepath, so you will need
to run it more than once if any of the intended files or folders have more than
one substring matching `old` (e.g. `my/old/directory/is/old.txt`).

`-path ./skipme -prune -o` means don't check the filenames within the `skipme`
directory. This isn't perfect, since it does not exclude the `skipme` directory
itself from being renamed if `-name`'s pattern matches it.
