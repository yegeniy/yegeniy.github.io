---
longtitle: Undo a release made by maven-release-plugin and git
categories: cli java_snippet
---

This command is a heuristic so it doesn't really care about the maven-release
plugin, basically it's supposed to check out the latest master branch, delete
the local and remote tags, and undo 2 commits.  It assumes that no one has
pushed any commits in since you made a release with [`maven-release-plugin`](http://maven.apache.org/maven-release/maven-release-plugin/examples/prepare-release.html).

```bash
function git-undo-mvn-release { TAG_NAME=$1 && echo "  >>> undoing last two commits and removing tag named '${TAG_NAME}' <<<" && echo "  getting and checking out the latest copy of the the master branch" && git fetch && git checkout master && git pull origin master && git status && echo "  removing local and remote tag ${TAG_NAME} if both exist" && git tag -d ${TAG_NAME} && git push origin :${TAG_NAME} && echo "  git reset HEAD^^ --hard" && git reset HEAD^^ --hard && git status && git log --oneline | head && echo '  >>> If you are satisfied, run `git push origin master --force` <<<'; }
```

Here is a sample of how it should work:

```bash
$ git log --oneline | head -n 1
b747131 a real commit... the one you want to get back to.

$ mvn release:prepare 
# ...

$ git log --oneline | head -n 3
db09eda [maven-release-plugin] prepare for next development iteration
00024fe [maven-release-plugin] prepare release test/1.0
b747131 a real commit... the one you want to get back to.

$ git-undo-mvn-release test/1.0
  >>> undoing last two commits and removing tag named 'test/1.0' <<<
  getting and checking out the latest copy of the the master branch
Already on 'master'
From https://github.com/yegeniy/yegeniy.github.io
 * branch            master     -> FETCH_HEAD
Already up-to-date.
On branch master
nothing to commit, working directory clean
git reset HEAD^^ --hard
HEAD is now at b747131 a real commit... the one you want to get back to.
  removing local and remote tag test/1.0 if both exist
Deleted tag 'test/1.0' (was db09eda)
To https://github.com/yegeniy/yegeniy.github.io.git
 - [deleted]         test/1.0
On branch master
nothing to commit, working directory clean
  >>> If you are satisfied, run `git push origin master --force` <<<

$ git log --oneline | head -n 1
b747131 a real commit... the one you want to get back to.

$ git push origin master # --force
```