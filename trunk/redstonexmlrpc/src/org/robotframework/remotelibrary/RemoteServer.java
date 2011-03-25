/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Redstone XML-RPC library is licensed under
 * GNU Library or Lesser General Public License (LGPL)
 */
package org.robotframework.remotelibrary;

import java.text.SimpleDateFormat;
import java.util.Date;

//get the following package(s) from http://xmlrpc.sourceforge.net/
import redstone.xmlrpc.simple.*;
//import redstone.xmlrpc.XmlRpcMessages;

/**
 * Java generic remote server implementation for
 * Robot Framework remote libraries implemented in Java.
 * 
 * NOTE: This is the first working implementation of the
 * remote server, using the Redstone XML-RPC w/ Simple HTTP
 * Server integration library package. There is an
 * abandoned Apache XML-RPC library non-working
 * implementation in the source code repository for those
 * that may be interested.
 *  
 * @author David Luu 
 *
 */
public class RemoteServer {
	
	public static String className;

	/** 
	 * Remote Server main/startup method.
	 * Takes input from command line for Java class library (name)
	 * to load and invoke with reflection and the port to bind
	 * the remote server to. Defaults to port 8270 if not supplied.
	 * @param args
	 * 
	 */
	public static void main(String[] args){// throws Exception {
		if(args.length == 0)
			displayUsage();
		
		int port = 8270; //default
		className = args[0];
		if(args.length == 2)
			port = Integer.parseInt(args[1]);
		
		//codebase below based on the Redstone XML-RPC with
		//Simple HTTP Server integration code example(s)
		
		try
        {
            server = new Server(port);
            //need to set invocation handler service name to null
            //to avoid dot notation for XML-RPC methods, (e.g. ClassName.MethodName)
            //to be compatible with Robot Framework remote library spec
            //http://sourceforge.net/projects/xmlrpc/forums/forum/79303/topic/2569828
            server.getXmlRpcServer().addInvocationHandler(null, new RemoteServerMethods());
            server.start();
            
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm");            
            Date date = new Date();
            System.out.println( "Robot remote server started on port " + port + " at " + bartDateFormat.format(date));
        }
        catch ( Exception e )
        {
            System.out.println(
                "An exception occured when starting the server. The full stacktrace " +
                "is displayed below. Check your arguments to verify that the server port " +
                "is available and that the service classes you supplied are in the classpath " +
                "and can be instantiated.\n\n" + 
                "Stacktrace:\n\n" );            
            e.printStackTrace();
        }        
	}  
    /** The Simple HTTP server and Redstone XML-RPC Library integration. */
    private static Server server;

	private static void displayUsage() {
		System.out.println("");
		System.out.println("robotremoteserver - v1.0");
		System.out.println("");
		System.out.println("Usage Info:");
		System.out.println("");
		System.out.println("  java -cp xmlrpc-1.1.1.jar:simple-4.0.1.jar:simple-xmlrpc-1.0.jar org.robotframework.remotelibrary.RemoteServer RemoteLibraryClassName-FullyQualifiedPath [port]");
		System.out.println("");
		System.out.println("  Class name = keyword class w/ methods to execute, fully qualified path");
		System.out.println("  including package name, as needed. Assumes class library (class/JAR");
		System.out.println("  file) is in class path for loading & invocation. As shown, server");
		System.out.println("  requires Redstone XML-RPC w/ Simple HTTP Server (JAR) libraries.");
		//comment out doc file info, until/unless we implement where some Javadoc file is required
		//System.out.println("  Documentation file = Java documentation file for the");
		//System.out.println("    class library.");
		System.out.println("");
		//commented out info about hostname/IP until we implement that, if ever
		//System.out.println("  Optionally specify IP address to bind remote server to.");
		//System.out.println("    Default of 127.0.0.1 (localhost).");
		System.out.println("  Optionally specify port to bind remote server to. Default of 8270.");
		System.out.println("");
		System.out.println("  NOTE: Would be nice if someone could build a working JAR version of server,");
		System.out.println("  so you don't have to execute from class files. I can't seem to do it myself.");
		System.out.println("");
		System.out.println("Example:");
		System.out.println("");
		System.out.println("  java -cp xmlrpc-1.1.1.jar:simple-4.0.1.jar:simple-xmlrpc-1.0.jar org.robotframework.remotelibrary.RemoteServer org.robotframework.remotelibrary.MyClassName 81");
		System.exit(0);
	}
}
