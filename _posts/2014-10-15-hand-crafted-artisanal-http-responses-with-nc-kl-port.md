---
longtitle: Hand-crafted artisanal HTTP responses via `nc -kl <port>`
categories: cli
---

Not only can you [listen for incoming HTTP Requests with netcat](#2014-05-09),
you can also be a good citizen and respond to them. The client making a HTTP
request will typically wait for the server to respond; although, it should
eventually time out.

Run `nc -kl $PORT` and when you see an incoming HTTP connection, paste or type
in the properly formatted HTTP response you want to provide when you see
incoming HTTP connections. This is useful for debugging clients that post
updates over a callback URI.

Many clients' APIs are cool with an empty response as long as it has status code
`200`:

```
HTTP/1.1 200 OK
Content-Length: 0

```
(note that for a proper HTTP response, you need two line feeds)

There is probably a simple way of piping netcat the response periodically,
without manually pasting it in; however, On OS X, 
```
while true; do echo -e $HTTP_RESPONSE | nc -l $PORT; done
```
would often result in an unexpected EOF error in my client from time to time.
