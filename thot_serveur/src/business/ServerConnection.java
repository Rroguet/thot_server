package business;
/**
 *  Main class, allows to launch the server.
 * @author jules
 *
 */
public class ServerConnection {
	public static void main (String[] args) {
		AbstractServer as = new FirstServer();
		String ip = "localhost";
		as.connect(ip);
		
	}
}
