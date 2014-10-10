---
longtitle: Fixing indendation in poms
categories: cli
---

```bash
export XMLLINT_INDENT="   "; for filepath in `find . -name pom.xml |xargs`; do xmllint --format --output $filepath $filepath; done
```