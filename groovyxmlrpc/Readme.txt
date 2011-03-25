This thread is interesting info...

http://groups.google.com/group/robotframework-users/browse_thread/thread/c1ce27fa439f5ad4

If Groovy compiled class files are no different than pure Java compiled class files, then one could implement the remote server using Groovy XML-RPC library which (at first glance) looks a lot simpler than the Apache, and somewhat Redstone XML-RPC Java libraries.

The implementation I believe could reuse the server library code base we have, and simply replace the RemoteServer class implementation with Groovy. Or alternatively, one could port the codebase to be an all Groovy implemenation, where possible.

However, I'm not a Groovy user, so I leave that open to the community if anyone is interested to provide an alternate implementation.

FYI, mentioned in thread link above as well, but Groovy XML-RPC library is here:

http://groovy.codehaus.org/XMLRPC