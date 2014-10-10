---
longtitle: Poll for results in separate thread
categories: java_snippet
---

```java
final ScheduledExecutor scheduler = Executors.newSingleThreadScheduledExecutor();
final AtomicReference<Result> value = new AtomicReference<Result>();
final CountDownLatch latch = new CountDownLatch(1);

scheduler.submit(new Runnable() {
    /* Retrieve a result by polling every 30 seconds */
    @Override
    public void run() {
        try {
            final Result pollResult = // poll resource for result;
            if (pollResult.isReady()) {
                value.set(pollResult);
                latch.countDown();
            } else {
                scheduler.schedule(this, 30L, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("Cancelling polling for result, because an unexpected Throwable occured:", e);
            latch.countDown();
        }
    }
});

// Blocks until a result is available
while(true) {
   try {
       latch.await();
       break;
   } catch (InterruptedException e) {
       log.error("Interrupted Exception occured:", e);
   }
}

return value.get();
```

In this contrived example, when a `Result` class is ready, its `isReady` method returns true. You could as easily check
for `null`, or something.

You could also use a Callable, which could throw the exception that is currently being logged.
