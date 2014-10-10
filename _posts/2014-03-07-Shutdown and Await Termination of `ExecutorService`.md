---
longtitle: Shutdown and Await Termination of [`ExecutorService`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html)
categories: java_snippet
---

```java
// From ExecutorService's JavaDoc
private static void shutdownAndAwaitTermination(final ExecutorService pool) {
    pool.shutdown(); // Disable new tasks from being submitted
    try {
        // Wait a while for existing tasks to terminate
        if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            pool.shutdownNow(); // Cancel currently executing tasks
            // Wait a while for tasks to respond to being cancelled
            if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                System.err.println("pool did not terminate");
        }
    } catch (InterruptedException ie) {
        // (Re-)Cancel if current thread also interrupted
        pool.shutdownNow();
        // Preserve interrupt status
        Thread.currentThread().interrupt();
    }
}
```

Consider using [`LOG::error`](http://www.slf4j.org/api/org/slf4j/Logger.html#error\(java.lang.String, java.lang.Object...\)) instead of `System.err::println`. 

Where `LOG` is:

```java
private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(CurrentClass.class);
```
