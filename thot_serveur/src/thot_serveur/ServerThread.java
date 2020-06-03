package thot_serveur;
import java.io.*;
import java.net.*;

import data.writeDataToXML;
import data.getDataFromXML;
import presentation.model.*;

public class ServerThread extends Thread {
    private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private getDataFromXML xml = new getDataFromXML();
	//private writeDataToXML xmlw = new writeDataToXML();
 
    public ServerThread(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {
        try {
			//create the streams that will handle the objects coming through the sockets
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
			writeDataToXML.initWriteDataToXML();
 
			
 			String login = (String)input.readObject();  //read the object received through the stream and deserialize it
			System.out.println("server received a login:" + login);
			String passWord = (String)input.readObject();  //read the object received through the stream and deserialize it
			System.out.println("server received a pass word:" + passWord);
			
			//writeDataToXML.newUser(new Utilisateur("f3","l3","u3",3,null), "loginU3", "passWordU3");
			writeDataToXML.addUserToConversation(3, 1);
			//writeDataToXML.newMessage(new Message(1,"test test test"), 1);
			Utilisateur u = xml.getUtilisateur(login, passWord);
			
			output.writeObject(u);
			
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();

		} catch (ClassNotFoundException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
			try {
				output.close();
				input.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
    }
}
