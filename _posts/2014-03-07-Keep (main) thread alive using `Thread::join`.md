---
category: java_snippet
---

This is _a_ way of keeping the main thread (or any other one really) alive. Not
always the right approach though.

```java
final ExecutorService stayAlive = Executors.newSingleThreadExecutor();
stayAlive.submit(new Runnable() {
    @Override
    public void run() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
        } finally {
            LOG.debug("resubmitting stayAlive runnable");
            stayAlive.submit(this);
        }
    }
});
```
