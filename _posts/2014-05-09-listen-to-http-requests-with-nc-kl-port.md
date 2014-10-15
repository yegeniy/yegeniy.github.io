---
longtitle: Listen to HTTP Requests with `nc -kl <port>`
categories: cli
---

This is not the best way of listening for HTTP requests, but netcat (`nc`) is
often available on linux servers and can be used to listen to an HTTP request on
a specific port. I found this useful for very basic verification of HTTP POSTs
initiated as part of a callback.