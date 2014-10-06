```
$ help wait
wait: wait [n]
    Wait for the specified process and report its termination status.  If
    N is not given, all currently active child processes are waited for,
    and the return code is zero.  N may be a process ID or a job
    specification; if a job spec is given, all processes in the job's
    pipeline are waited for.
```

```bash
echo 5 concurrent sleep cycles later
for i in {1..5}; do 
  { # perform this block of commands in the background 
  sleep 1; echo -n '.'
  } &
done
wait
echo "we are up"
```