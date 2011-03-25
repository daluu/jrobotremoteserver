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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used to shut down the Java generic
 * remote server for Robot Framework
 * 
 * As defined by Robot Framework spec, this 
 * will remotely stop remote library server.
 * 
 * To be called by Robot Framework remote library interface
 * or by XML-RPC request to run_keyword() XML-RPC method
 * (in RemoteServerMethods class), passing it 
 * "stop_remote_server" as single argument.
 * 
 * But this class can also be reused for other purposes as well.
 * 
 * @author David Luu
 *
 */
public class Shutdown implements Runnable {
	private int delay;
	public Shutdown(){ //default
		delay = 60000; //let's arbitrarily set delay at 1 minute
	}
	public Shutdown(int pDelay){
		delay = pDelay;
	}
	public void run(){
		try {
			System.out.println("Shutting down remote server/library, from Robot Framework/XML-RPC request, in 1 minute.");
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm");            
        Date date = new Date();
		System.out.println("Remote server/library shut down at " + bartDateFormat.format(date));
		System.exit(0);
	}
}
