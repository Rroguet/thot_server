package business;

import java.io.*;  
import java.net.*;

import singletons.Singletons;


/**
 * Starts server and waits for connection
 * @author jules
 * 
 */
public class FirstServer extends AbstractServer{
	private ServerSocket ss;
	/**
	 * Allows connection to the server.
	 */
	public void connect(String ip) {
		try {
			//the server socket is defined only by a port (its IP is localhost)
			ss = new ServerSocket (6667);  
			Singletons.getlogsWriter().write("Server waiting for connection...\n");
			while (true) {
				Socket socket = ss.accept();//establishes connection 
				Singletons.getlogsWriter().write("Connected as " + ip+"\n");	
				// create a new thread to handle client socket	
				new ServerThread(socket).start();				
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			//if IOException close the server socket
			if (ss != null && !ss.isClosed()) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
}
