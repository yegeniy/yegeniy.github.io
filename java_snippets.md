---
layout: index
title: Java Snippets by yegeniy
shorttitle: Java Snippets
tagline: Stuff that's hard to look up<br/>Candidates for utility classes
---

{% for post in site.posts %}
{% if post.categories contains 'java_snippet' %}
{% if post.date != post.next.date  %}
<a name="{{post.date | date: "%Y-%m-%d"}}" href="#{{post.date | date: "%Y-%m-%d"}}">
{{post.date | date: "%Y-%m-%d"}}
-------------
</a>
{% endif %}

##### {{post.longtitle}}
{{post.content}}
{% endif %}
{% endfor %}
