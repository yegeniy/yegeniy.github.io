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

There is probably a simple way of piping the response periodically to netcat,
without manually pasting it in; however, On OS X, 
```
while true; do echo -e $HTTP_RESPONSE | nc -l $PORT; done
```
would often result in an unexpected EOF error in my client from time to time.

2019-11-17 Update.
There is a way to simulate `nc -e` on OS X, but it's 
[not a one-liner](https://stackoverflow.com/a/24342101/2916086). More like 10.

```bash
#!/usr/bin/env bash
port=$1; shift # 123
cmd=$1; # "date +%s"

rm -f /tmp/f; mkfifo /tmp/f; trap "rm -f /tmp/f; rm -f /tmp/f; exit 0" 2;

while true; do cat /tmp/f | nc -l ${port} > >(read line;
  CONTENT=$(eval "${cmd}")
  echo -e "HTTP/1.1 200 OK\nContent-Length: ${#CONTENT}\n\n${CONTENT}" > /tmp/f)
done;
```
