Start Tomcat with `catalina jpda start`. It will start Tomcat so that a remote debugger can be connected to port 8000.

Set up your debugger to run with the following options when the JVM is started:

    -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n

In IDEA, this done in the Debug Configurations by setting up the "Remote" configuration.

http://wiki.apache.org/tomcat/FAQ/Developing#Q1
http://stackoverflow.com/questions/11480563/debugging-with-tomcat-and-intellij-community-edition