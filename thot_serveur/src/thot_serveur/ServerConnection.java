package thot_serveur;

import business.AbstractServer;
import business.FirstServer;

public class ServerConnection {
	public static void main (String[] args) {
		AbstractServer as = new FirstServer();
		String ip = "localhost";
		as.connect(ip);
		
	}
}
