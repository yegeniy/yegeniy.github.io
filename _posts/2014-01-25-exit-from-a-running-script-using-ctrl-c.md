---
longtitle: Exit from a running script using `Ctrl+C`
categories: cli
---

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