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
 
    public ServerThread(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {
        try {
			//create the streams that will handle the objects coming through the sockets
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
 
			String action = (String)input.readObject();
			
			if(action.equals("login")) login();
			if(action.equals("newMessage")) newMessage();
			if(action.equals("newUser")) newUser();
			if(action.equals("newConv")) newConv();
			if(action.equals("addUser")) addUser();
						//writeDataToXML.newUser(new Utilisateur("f3","l3","u3",3,null), "loginU3", "passWordU3");
			//writeDataToXML.addUserToConversation(3, 1);
			//writeDataToXML.newMessage(new Message(1,"test test test"), 1);
			//Utilisateur u = xml.getUtilisateur(login, passWord);
			Conversation conv = xml.getConvFromID(1);
			System.out.println("Conversation found : " + conv.getName());
			//output.writeObject(u);
			output.writeObject(conv);

			
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
    
    public void login() {
    	try {
    		String login = (String)input.readObject();  //read the object received through the stream and deserialize it
    		System.out.println("server received a login:" + login);
    		String passWord = (String)input.readObject();  //read the object received through the stream and deserialize it
    		System.out.println("server received a pass word:" + passWord);
		
    		Utilisateur u = xml.getUtilisateur(login, passWord);
		
    		output.writeObject(u);
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    
    public void newMessage() {
    	try {
    		Message m = (Message) input.readObject();  
    		int convId = (int)input.readObject();
    				
    		writeDataToXML.newMessage(m, convId);
    		output.writeObject(true);
    		System.out.println("new message saved");
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    
    public void newUser() {
    	try {
    		String login = (String)input.readObject();
    		String passWord = (String)input.readObject();
    		Utilisateur u = (Utilisateur) input.readObject();
		
    		writeDataToXML.newUser(u, login, passWord);
    		output.writeObject(true);
    		System.out.println("new user saved");
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    
    public void newConv() {
    	try {
    		Conversation c = (Conversation)input.readObject();
		
    		writeDataToXML.newConv(c);
    		output.writeObject(true);
    		System.out.println("new conversation saved");
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    
    public void addUser() {
    	try {
    		int userId = (int)input.readObject();
    		int convId = (int)input.readObject();
		
    		writeDataToXML.addUserToConversation(userId, convId);
    		output.writeObject(true);
    		System.out.println("user added to conversation");
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
}
