If you have internal dependencies (e.g. multi-module project), you should run
`mvn install` _on that dependency_ before running `mvn package` on the multi-
module project. At least the first time, to get the dependencies installed into
your local repository (under `~/.m2/repository/`).
