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

//following 3 libraries used for XML-RPC struct & array data I/O
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import java.lang.reflect.*; //for the get_keyword methods and run_keyword method
import java.lang.annotation.Annotation; //for get_keyword_documentation method
//import java.text.Annotation;			//use this one as well, or use instead?
//may also want to extend this library to use & import javadoc and/or doclets API, etc.

/**
 * XML-RPC server methods implementation for the 
 * Java generic remote server for Robot Framework
 * for calling remote libraries implemented Java.
 * 
 * Based on RobotFramework spec at
 * http://code.google.com/p/robotframework/wiki/RemoteLibrary
 * http://robotframework.googlecode.com/svn/tags/robotframework-2.5.6/doc/userguide/RobotFrameworkUserGuide.html#remote-library-interface
 * http://robotframework.googlecode.com/svn/tags/robotframework-2.5.6/doc/userguide/RobotFrameworkUserGuide.html#dynamic-library-api
 * 
 * Uses Java reflection to serve the dynamically loaded remote Java class library.
 * You may alternatively modify this starting code base to natively integrate
 * your Java test library code into the server rather than load it dynamically.
 * 
 * @author David Luu
 *
 */
public class RemoteServerMethods {
	
	/**
	 * Get a list of RobotFramework keywords available in remote library for use.
	 * @return list of keywords in Java remote library
	 */
	public String[] get_keyword_names(){
		try {			
			Class cls = Class.forName(RemoteServer.className);	        
			Method methlist[] = cls.getDeclaredMethods();
			String[] keywords = new String[methlist.length+1];
			for (int i = 0; i < methlist.length; i++) {  
				Method m = methlist[i];
				keywords[i] = m.getName();
			}
			//add stop_remote_server to the list, you don't
			//have to implement it in the remote library
			keywords[methlist.length] = "stop_remote_server";
			return keywords;
		}
		catch (Throwable e) {
			System.err.println(e);
			return null;
		}
	}
	
	/**
	 * Run specified Robot Framework keyword from remote server.
	 * @param keyword Keyword class library method to run for Robot Framework.
	 * @param args Arguments, if any, to pass to keyword method. An XML-RPC array or in Redstone library, a Java ArrayList.
	 * @return RobotFramework specified data structure indicating pass/fail. An XML-RPC struct or in Redstone library, a Java HashMap.
	 */
	public Map run_keyword(String keyword, ArrayList args){
		HashMap kr = new HashMap();
		try {
			if(keyword.equalsIgnoreCase("stop_remote_server")){
				//spawn new thread to do a delayed server shutdown
				//and return XML-RPC response before delay is over
				Shutdown sd = new Shutdown(); //special class to do shutdown
				Thread stop = new Thread(sd);
				stop.start();
				kr.put("status", "PASS"); //RobotFramework spec for shutdown
				kr.put("return", 1);
				kr.put("error","");
				kr.put("output","");
				kr.put("traceback","");
				return kr;
			}
			Class cls = Class.forName(RemoteServer.className);
			Method methlist[] = cls.getDeclaredMethods();
			Class[] paramTypes = null;
			String retType = null;
			for (int i = 0; i < methlist.length; i++) {  
				Method m = methlist[i];
				if(m.getName().equalsIgnoreCase(keyword)){
					paramTypes = m.getParameterTypes();
					retType = m.getReturnType().toString();
				}
			}
		    Method meth = cls.getMethod(keyword, paramTypes);
		    Object retObj = cls.getMethod(keyword, paramTypes).invoke(cls.newInstance(), args.toArray());
		    //TODO - check return type = array of some object, 
		    //or simple types: int, String, boolean, etc.
		    //and process return value accordingly to send back to caller
		    //use .NET version of generic remote server as the model to follow
		    //http://code.google.com/p/sharprobotremoteserver/
		    
		    //so for now, we do this...
		    
		    //due to limitation of Java? (I think) in not being able to redirect
			//standard (or stream) output from reflected/loaded library
			//output will always be empty with this implementation. Until we can
			//fix/optimize this deficiency.
			kr.put("error","");
			kr.put("output","");
			kr.put("traceback","");
		    kr.put("status", "PASS");  //always pass, if no exception, RF spec
		    if(retObj == null)
		    	kr.put("return", ""); //can't return null, so do this...
		    else
		    	kr.put("return", retObj);
		    return kr;
		}
		catch (Throwable e) {
			System.err.println(e);
			kr.put("status", "FAIL");
			kr.put("return", "");
			kr.put("error",e.getMessage());
			kr.put("output",e.getMessage());
			String stktrc = "";
			StackTraceElement[] st = e.getStackTrace();
			for(int i = 0; i < st.length; i++){
				stktrc += st.toString();
				kr.put("traceback",stktrc);
			}
			return kr;
		}
	}
	
	/**
	 * Get list of arguments for specified Robot Framework keyword.
	 * 
	 * NOTE: Currently returns argument data type instead of name
	 * until we can fix it. 
	 * @param keyword The keyword to get a list of arguments for.
	 * @return A string array of arguments for the given keyword.
	 */
	public String[] get_keyword_arguments(String keyword){
		//TODO - figure out how to best to implement this
		//with reflection. Given a "keyword" method name,
		//lookup method in Java class library that's 
		//loaded into the server, and return a string array
		//of arguments (names) that method takes.
		
		//my knowledge of Java reflection is limited,
		//so for now, we do this...
		try {			
			Class cls = Class.forName(RemoteServer.className);
			Method methlist[] = cls.getDeclaredMethods();
			String[] args = null;
			for (int i = 0; i < methlist.length; i++) {  
				Method m = methlist[i];
				if(m.getName().equalsIgnoreCase(keyword)){
					Class[] argsTmp = m.getParameterTypes();
					args = new String[argsTmp.length];
					for (int j = 0; j < args.length; j++) {
						args[j] = argsTmp[j].toString();
					}
				}
			}
			return args;
		}
		catch (Throwable e) {
			System.err.println(e);
			return null;
		}
	}
	
	/**
	 * Get documentation for specified Robot Framework keyword.
	 * 
	 * NOTE: Tentatively done by getting class annotation info
	 * plus return value of keyword method. Could enhance to
	 * parse Javadoc info, etc.
	 * @param keyword The keyword to get documentation for.
	 * @return A documentation string for the given keyword.
	 */
	public String get_keyword_documentation(String keyword){
		//TODO - figure out how to implement this.
		//Given a "keyword" method name, lookup method's
		//documentation, and return as string.
		//Use Javadoc API? Use doclet API?
		//Use Java annotations?
		//Look into how Robot Framework (non-remote)
		//Java/Jython libraries implement this
		//(with annotations, I believe)
		
		//my knowledge of Java reflection +
		//Java documentation is limited,
		//so for now, we do this...
		try {
			String doc = null;
			Class cls = Class.forName(RemoteServer.className);
			Method methlist[] = cls.getDeclaredMethods();
			for (int i = 0; i < methlist.length; i++) {  
				Method m = methlist[i];
				if(m.getName().equalsIgnoreCase(keyword)){
					Annotation[] ann = m.getDeclaredAnnotations();
					doc += "Annotations, if any:\n\n";
					for (int j = 0; j < ann.length; j++) {
						doc += ann[j].toString() + "\n";
					}
					doc += "\nReturns: " + m.getReturnType().getSimpleName();
				}
			}
			return doc;
		}
		catch (Throwable e) {
			System.err.println(e);
			return null;
		}
	}
}
