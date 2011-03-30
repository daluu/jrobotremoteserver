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
 */
package org.robotframework.remotelibrary;

import java.net.InetAddress;
//get the following packages from
//http://ws.apache.org/xmlrpc/index.html
import org.apache.xmlrpc.common.TypeConverterFactoryImpl;
//import org.apache.xmlrpc.demo.webserver.proxy.impls.AdderImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
//import org.apache.xmlrpc.webserver.ServletWebServer;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * Java generic remote server implementation for
 * Robot Framework remote libraries implemented in Java.
 * 
 * NOTE: this is a version that uses Apache XML-RPC library
 * but in initial development, I had issues with references
 * to code or library calls and couldn't compile successfully.
 * Must have missed something. Anyways, tried Redstone library
 * and that seemed to work more easily, so switched to that one.
 * But I post the initial codebase for the Apache version
 * in case anyone is interested in fixing this up so that the
 * server can run with either library. If you do get it running,
 * would be preferable to distribute compiled binary files
 * (class files or JAR) so that others may use in case they
 * can't easily compile like me.
 * 
 * @author David Luu 
 *
 */
public class RemoteServer {
	
	public static String className;
	public static boolean enableStopServer;

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
		
		//set defaults
		int port = 8270;
		className = "org.robotframework.remotelibrary.ExampleRemoteLibrary";
		enableStopServer = true;

		//parse arguments
		for(int i = 0; i < args.length; i++){
			if(args[i].equalsIgnoreCase("--library"))
				className = args[i+1];
			if(args[i].equalsIgnoreCase("--port"))
				port = Integer.parseInt(args[i+1]);
			if(args[i].equalsIgnoreCase("--nostopsvr"))
				enableStopServer = false;
		}
		
		//the code blocks below are based on 
		//Apache library examples at
		//http://ws.apache.org/xmlrpc/server.html
		
		//Servlet approach
		//XmlRpcServlet servlet = new XmlRpcServlet();
		//ServletWebServer webServer = new ServletWebServer(servlet, port);
		
		//Web server approach
		WebServer webServer = new WebServer(port);
		XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
		
		PropertyHandlerMapping phm = new PropertyHandlerMapping();
        /* Load handler definitions from a property file.
         * The property file might look like:
         *   Calculator=org.apache.xmlrpc.demo.Calculator
         *   org.apache.xmlrpc.demo.proxy.Adder=org.apache.xmlrpc.demo.proxy.AdderImpl
         */
        //phm.load(Thread.currentThread().getContextClassLoader(),"MyHandlers.properties");

        /* You may also provide the handler classes directly,
         * like this:
         */ 
        phm.addHandler("RemoteServerMethods",org.robotframework.remotelibrary.RemoteServerMethods.class);
        //phm.addHandler(org.apache.xmlrpc.demo.proxy.Adder.class.getName(),org.apache.xmlrpc.demo.proxy.AdderImpl.class);

        xmlRpcServer.setHandlerMapping(phm);
      
        XmlRpcServerConfigImpl serverConfig =
            (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        webServer.start();
	}

	private static void displayUsage() {
		System.out.println("");
		System.out.println("robotremoteserver - v1.0");
		System.out.println("");
		System.out.println("Usage Info:");
		System.out.println("");
		System.out.println("  java -cp xmlrpc-common-3.1.3.jar:xmlrpc-server-3.1.3.jar:ws-commons-util-1.0.2.jar:commons-logging-1.1.jar org.robotframework.remotelibrary.RemoteServer --library RemoteLibraryClassName-FullyQualifiedPath [options]");
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
		System.out.println("Optional options:");
		System.out.println("");
		System.out.println("  --port value\tPort to bind remote server to. Default of 8270.");
		System.out.println("");
		System.out.println("  --nostopsvr\tDisable remotely stopping the server via stop_remote_server");
		System.out.println("             \tkeyword. Default is to enable.");
		System.out.println("");
		System.out.println("  NOTE: Would be nice if someone could build a working JAR version of server,");
		System.out.println("  so you don't have to execute from class files. I can't seem to do it myself.");
		System.out.println("");
		System.out.println("Example:");
		System.out.println("");
		System.out.println("  java -cp xmlrpc-common-3.1.3.jar:xmlrpc-server-3.1.3.jar:ws-commons-util-1.0.2.jar:commons-logging-1.1.jar org.robotframework.remotelibrary.RemoteServer --library org.robotframework.remotelibrary.MyClassName --port 81 --nostopsvr");
		System.exit(0);
	}
}
