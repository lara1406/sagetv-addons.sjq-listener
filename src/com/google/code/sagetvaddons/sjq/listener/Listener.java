/*
 *      Copyright 2010 Battams, Derek
 *       
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 */
package com.google.code.sagetvaddons.sjq.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

/**
 * @author dbattams
 *
 */
public final class Listener {
	static private final Logger LOG = Logger.getLogger(Listener.class);
	
	private String cmdPkg;
	private int port;
	private ServerSocket srvSocket;

	public Listener(String cmdPkg, int port) {
		this.cmdPkg = cmdPkg;
		this.port = port;
	}
	
	public void init() throws IOException {
		srvSocket = new ServerSocket(port);
		srvSocket.setSoTimeout(5000);
		while(true) {
			try {
				Socket s = srvSocket.accept();
				LOG.info("Received connection from: " + s.getInetAddress());
				Thread t = new Thread(new Handler(s, cmdPkg));
				t.setDaemon(true);
				t.start();
			} catch(SocketTimeoutException e) {
				
			}
			if(Thread.interrupted()) {
				srvSocket.close();
				LOG.warn("Shutting down listener...");
				break;
			}
		}
	}
}
