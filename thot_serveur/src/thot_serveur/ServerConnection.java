package thot_serveur;

import data.writeDataToXML;

public class ServerConnection {
	public static void main (String[] args) {
		writeDataToXML.initWriteDataToXML();
		AbstractServer as = new FirstServer();
		String ip = "localhost";
		as.connect(ip);
		
	}
}
