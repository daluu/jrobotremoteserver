

# Installation #

## Prerequisites ##

  * [Redstone XML-RPC + Simple HTTP Server intgration JAR packages](http://xmlrpc.sourceforge.net/)
  * Example Java remote library, bundled with the generic server, or your own Java remote (class) library, for use with [Robot Framework](http://www.robotframework.org).
  * If using with [Robot Framework](http://www.robotframework.org) locally, running server on localhost, then need to install [Robot Framework](http://www.robotframework.org).
  * A Java compiler (JDK) and/or IDE (Eclipse IntelliJ Idea, Netbeans, etc.) if you wish to edit and compile the source and/or package it into a JAR.

## Installation procedure ##

As the remote server acts as a remote library for [Robot Framework](http://www.robotframework.org), no installation is needed. But the prerequisites have to be met first.

  1. Download [Redstone XML-RPC + Simple HTTP Server intgration JAR packages](http://xmlrpc.sourceforge.net/), if needed.
  1. Download the [generic remote server binary package](http://jrobotremoteserver.googlecode.com/files/jrobotremoteserver-1.0r1.zip), which already contains Redstone JAR files.
  1. Extract to desired location.
  1. Copy over [Redstone XML-RPC + Simple HTTP Server intgration JAR packages](http://xmlrpc.sourceforge.net/) to extracted location, if needed.

You may add additional Java libraries (class or JAR files) in extracted location to start the remote server with those libraries instead of the provided example library.

**UPDATE:** If you use the JAR file version, all the requirements/dependencies are already packaged into the JAR, all you just need then is the Java runtime. If using your own test library with the server, you may want to repackage the JAR with your files in it. See the how to file in the page for details.

# Usage instructions #

Run **robotremoteserver** passing it class path references to the [Redstone XML-RPC + Simple HTTP Server intgration JAR packages](http://xmlrpc.sourceforge.net/) and then the fully qualified name of Java class library that contains the implementation of keywords (e.g. actual remote library). You can also run server without arguments to get this usage info.

Server also takes optional parameter for port to bind to, or it will default to the 8270.

See [RemoteServerDetails#Testing\_the\_example\_remote\_library](RemoteServerDetails#Testing_the_example_remote_library.md) for an example of usage.

# Testing the example remote library #

Run robotremoteserver with the examplelibrary like follows:

> `java -cp .;xmlrpc-1.1.1.jar;simple-xmlrpc-1.0.jar;simple-4.0.1.jar org.robotframework.remotelibrary.RemoteServer`

> `--library org.robotframework.remotelibrary.ExampleRemoteLibrary`

or if using the JAR version:

> `java -jar jrobotremoteserver-1.0.jar --library org.robotframework.remotelibrary.ExampleRemoteLibrary`

Then run the [example tests](http://robotframework.googlecode.com/svn/trunk/tools/remoteserver/example/remote_tests.html) for remote libraries/servers available from [Robot Framework](http://www.robotframework.org) project.

To test stop remote server functionality, you may wish to add a test case, test step, or test case/suite teardown like this:

| Test Case | Action | Argument |
|:----------|:-------|:---------|
| Stop Server Test | Run Keyword | Stop Remote Server |

# Java remote library interface with the generic remote server #

The generic remote server uses Java reflection to access the actual remote library during runtime. Alternatively, you may wish to integrate the library code into the remote server and make it non-generic rather than use reflection/dynamic loading.

The remote server includes keyword **stop\_remote\_server** so you don't have to implement that in the remote library.

Remote library methods should conform to [Robot Framework](http://www.robotframework.org) keyword API specification, meaning: methods should be named as **method\_name()** rather than MethodName() or methodName(); the underscore represents a space; the method is made available as a keyword in [Robot Framework](http://www.robotframework.org) named **Method Name**. Alternatively, they might not have to follow this convention, but you would have to modify the remote server to be able to translate the Robot Framework keyword naming convention to the actual Java method naming convention when XML-RPC calls are made to **run\_keyword**.

**NOTE:** the remote server currently does not implement or use the JavaLibCore library package for Robot Framework, therefore, you can't build Java test libraries for the remote server that way. You can only build them following the Robot Framework simple test library and Java reflection methods. See ([Issue 7](http://code.google.com/p/jrobotremoteserver/issues/detail?id=7)) for details.

Additionally, the library's use of data types in keyword arguments or return values should conform to the [XML-RPC](http://www.xmlrpc.com/spec) protocol and what is supported by [Robot Framework](http://www.robotframework.org).

## Designing new custom test libraries in Java ##

For this case, you need only follow the Java remote library interface guidelines when creating your test library for it to be callable from [Robot Framework](http://www.robotframework.org).

**NOTE:** the remote server currently does not implement or use the JavaLibCore library package for Robot Framework, therefore, you can't build Java test libraries for the remote server that way. You can only build them following the Robot Framework simple test library and Java reflection methods. See ([Issue 7](http://code.google.com/p/jrobotremoteserver/issues/detail?id=7)) for details.

## Re-using existing Java libraries for Robot Framework ##

For this case, you would need to write a wrapper class library that provides the remote library interface on the front end and makes the actual calls to the desired Java code/class (or web service) on the back end, for it to be callable from [Robot Framework](http://www.robotframework.org).

And one might be able to use the Java libraries already built for Robot Framework. They may work under this remote server as a remote library as well. See [Robot Framework Test Libraries page](http://code.google.com/p/robotframework/wiki/TestLibraries) for possible Java libraries that can be used here. But see following note...

**NOTE:** The remote server currently does not implement or use the JavaLibCore library package for Robot Framework, therefore, you can't use Java test libraries build that way with the remote server. One such example is the [SwingLibrary](http://code.google.com/p/robotframework-swinglibrary/). See ([Issue 7](http://code.google.com/p/jrobotremoteserver/issues/detail?id=7)) for details.

## Packaging test libraries with the remote server together ##

You can just archive/zip up the needed class files and JARs, or you can bundle everything into a single JAR for easy distribution and execution. Here are the steps, thanks to user bocadillodeatun.

> `# Extract the original package and/or navigate to bin folder`

> `unzip jrobotremoteserver-1.0_bin.zip`

> `cd bin`

> `# add your test libraries within this bin folder structure if not already`

> `# Extract the bundled jars`

> `jar xf simple-4.0.1.jar`

> `jar xf simple-xmlrpc-1.0.jar`

> `jar xf xmlrpc-1.1.1.jar`

> `# Create the new jar containing everything`

> `jar cvfe jrobotremoteserver-1.0.jar org/robotframework/remotelibrary/RemoteServer org/ redstone/`

> `# Now can execute the newly created jar as follows...`

> `java -jar jrobotremoteserver-1.0.jar --library org.robotframework.remotelibrary.ExampleRemoteLibrary`

# Known Issues #

  * Wrong exception when exception thrown for ExampleRemoteLibrary ([Issue 2](http://code.google.com/p/jrobotremoteserver/issues/detail?id=2))

  * keyword output will always be blank except in cases of exceptions being raised. Currently no real definition of what constitutes "output" from a keyword. ([Issue 3](http://code.google.com/p/jrobotremoteserver/issues/detail?id=3))

  * Get keyword arguments returns argument data type but not name ([Issue 4](http://code.google.com/p/jrobotremoteserver/issues/detail?id=4))

  * remote library keywords will always return as PASS except for those that have return type as boolean and return false, and in case of exceptions

  * no option to bind by IP address or hostname. Doesn't seem to be offered by the XML-RPC Java libraries I'm using.

  * keyword documentation retrieval attempts to get from annotations and via Java reflection to get return type, etc. but it likely doesn't work the same way as Robot Framework's native Java library integration method. So changes need to be made in this area if one desires the same functionality for Java remote server libraries. See [DeveloperDiscussions#Keyword\_library\_documentation](DeveloperDiscussions#Keyword_library_documentation.md) for details.

# Tips for Debugging #

  * You can use a REST client (like popular browser extensions) to pass correct [XML-RPC](http://www.xmlrpc.com/spec) requests (an HTTP POST with data payload in XML-RPC format) and validate correct XML response from remote library.

  * You can use [Robot Framework](http://www.robotframework.org) tests to make the XML-RPC calls and validate correct responses, etc. if you are not good with XML-RPC messaging.

  * For Java class library related issues, you can build other Java apps to load the same library being used with remote server, or implement a main method to the class library and test to make sure library works correctly before loading into server at runtime.