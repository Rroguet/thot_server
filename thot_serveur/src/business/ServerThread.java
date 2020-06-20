package business;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import data.writeDataToXML;
import data.getDataFromXML;
import presentation.model.*;
/**
 * Serverside class handling received action from the client.
 * @author jules
 * 
 */
public class ServerThread extends Thread {
    private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	public ServerThread(Socket socket) {
        this.socket = socket;
    }
	/**
	 * As long as the server is connected, handles actions coming from the user.
	 */
    public void run() {
        try {
			//create the streams that will handle the objects coming through the sockets
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
 
			String action;
			do {
				action = (String)input.readObject();
			
				if(action.equals("login")) login();
				if(action.equals("newMessage")) newMessage();
				if(action.equals("newUser")) newUser();
				if(action.equals("newConv")) newConv();
				if(action.equals("addUser")) addUser();
				if(action.equals("getConvNames")) getUserConvNameList();
				if(action.equals("getConv")) getSelectedConvById();
				if(action.equals("getMembers")) getUsersNames();
			} while (socket.isConnected());
			
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        } finally {
			try {
				output.close();
				input.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
    }
    /**
     * Reads input from user login and outputs corresponding user info to the stream.
     */
    public void login() {
    	try {
    		String login = (String)input.readObject();  //read the object received through the stream and deserialize it
    		String passWord = (String)input.readObject();  //read the object received through the stream and deserialize it
		
    		Utilisateur u = getDataFromXML.getUtilisateur(login, passWord);
    		output.writeObject(u);
    		
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    /**
     * Reads input message from user and stores message in XML.
     */
    public void newMessage() {
    	try {
    		Message m = (Message) input.readObject();  
    		UUID convId = (UUID)input.readObject();
    				
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
    /**
     * Reads info for a new user, then adds it into userXML.
     */
    public void newUser() {
    	try {
    		String login = (String)input.readObject();
    		String passWord = (String)input.readObject();
    		Utilisateur u = (Utilisateur) input.readObject();
		
    		//Outputs true if user was created successfully.
    		output.writeObject(writeDataToXML.newUser(u, login, passWord));
    		System.out.println("new user saved");
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    /**
     * Reads info for a new conversation, then adds it to conversation XML.
     */
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
    /**
     * Reads a userID and a convID, then adds the user to the conversation's user list.
     */
    public void addUser() {
    	try {
    		String userId = (String)input.readObject();
    		UUID convId = (UUID)input.readObject();
		
    		//Outputs true if user was successfully added to conversation.
    		output.writeObject(writeDataToXML.addUserToConversation(userId, convId));
    		//System.out.println("user added to conversation"); REPLACE WITH LOGS
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    /**
     * Reads a userId, then outputs corresponding user's list of conversations' names to the stream.
     */
    public void getUserConvNameList() {
    	try {
    		UUID userId = (UUID)input.readObject();
    		Utilisateur u = getDataFromXML.getUserById(userId);
    		List<String> convNames = getDataFromXML.getConvNamesOfUser(u);
    		output.writeObject(convNames);
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    /**
     * Reads a conversation ID, then outputs corresponding conversation object to the stream.
     */
    public void getSelectedConvById() {
    	try {
    		UUID convId = (UUID)input.readObject();
    		Conversation conv = getDataFromXML.getConvById(convId);
    		output.writeObject(conv);
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
    /**
     * Reads a conversation ID, then outputs the names of every user participating in the conversation.
     */
    public void getUsersNames() {
    	//TODO output the list of all users' names.
    	try {
    		@SuppressWarnings("unchecked")
			List<UUID> usersId = (List<UUID>)input.readObject();
    		List<String> usersNames = new ArrayList<String>();
    		for (UUID i : usersId) {
    			usersNames.add(getDataFromXML.getUserById(i).getUserName());
    		}
    		output.writeObject(usersNames);
    	} catch (IOException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();

    	} catch (ClassNotFoundException ex) {
    		System.out.println("Server exception: " + ex.getMessage());
    		ex.printStackTrace();
    	}
    }
}
