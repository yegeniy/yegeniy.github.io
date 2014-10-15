---
longtitle: A Blocking Drain for `BlockingQueue`
categories: java_snippet
---

```java
final BlockingQueue<Foo> outbound;
final Collection<Foo> outboundElements;

while(true) {
    outboundElements.add(outbound.take());
    outbound.drainTo(outboundElements);
    // Work with the contents of outboundElements for a while...
    outboundElements.clear();    
}
```

BlockingQueue::drainTo is not blocking, and BlockingQueue::take only takes a
single element. Putting the two together could be useful for applications that
have periods of high activity, low activity, and no activity at arbitrary times.
This should help keep the `outbound` queue from filling up too much during busy
periods, while preventing your application from going through the work step
repeatedly with zero elements.

Note that this isn't a very efficient algorithm, for when elements first start
coming in. So, if the work you need to do is so expensive that doing it twice
when you could do it once is prohibitive, consider exploring a more
sophisticated approach.
