---
layout: index
title: Tricks for the CLI by yegeniy
shorttitle: Tricks for the CLI
tagline: Mostly bash and java and OS X
---

{% for post in site.posts %}
{% if post.date != post.next.date  %}
<a name="{{post.date | date: "%Y-%m-%d"}}" href="#{{post.date | date: "%Y-%m-%d"}}">
{{post.date | date: "%Y-%m-%d"}}
-------------
</a>
{% endif %}

##### {{post.title}}
{{post.content}}
{% endfor %}