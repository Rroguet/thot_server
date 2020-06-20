package business;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import data.writeDataToXML;
import data.getDataFromXML;
import presentation.model.*;
import singletons.Singletons;
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
        	try {
				Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
			} catch (IOException e) {
			}
		} catch (ClassNotFoundException ex) {
			try {
				Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
			} catch (IOException e) {
			}
        } finally {
			try {
				output.close();
				input.close();
			} catch (IOException ioe) {
			}
		}
    }
    /**
     * Reads input from user login and outputs corresponding user info to the stream.
     * @throws IOException 
     */
    public void login() throws IOException {
    	try {
    		String login = (String)input.readObject();  //read the object received through the stream and deserialize it
    		String passWord = (String)input.readObject();  //read the object received through the stream and deserialize it
		
    		Utilisateur u = getDataFromXML.getUtilisateur(login, passWord);
    		output.writeObject(u);
    		
    	} catch (IOException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
    /**
     * Reads input message from user and stores message in XML.
     * @throws IOException 
     */
    public void newMessage() throws IOException {
    	try {
    		Message m = (Message) input.readObject();  
    		UUID convId = (UUID)input.readObject();
    				
    		writeDataToXML.newMessage(m, convId);
    		output.writeObject(true);
    		Singletons.getlogsWriter().write("new message saved"+"\n");
    	} catch (IOException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
    /**
     * Reads info for a new user, then adds it into userXML.
     * @throws IOException 
     */
    public void newUser() throws IOException {
    	try {
    		String login = (String)input.readObject();
    		String passWord = (String)input.readObject();
    		Utilisateur u = (Utilisateur) input.readObject();
		
    		//Outputs true if user was created successfully.
    		output.writeObject(writeDataToXML.newUser(u, login, passWord));
    		Singletons.getlogsWriter().write("new user saved : " + u.getId()+"\n");
    	} catch (IOException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
    /**
     * Reads info for a new conversation, then adds it to conversation XML.
     * @throws IOException 
     */
    public void newConv() throws IOException {
    	try {
    		Conversation c = (Conversation)input.readObject();
    		//Outputs true if conv successfully created.
    		output.writeObject(writeDataToXML.newConv(c));
    		Singletons.getlogsWriter().write("new conversation saved : "+ c.getConvId()+"\n");
    	} catch (IOException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
    /**
     * Reads a userID and a convID, then adds the user to the conversation's user list.
     * @throws IOException 
     */
    public void addUser() throws IOException {
    	try {
    		String userId = (String)input.readObject();
    		UUID convId = (UUID)input.readObject();
		
    		//Outputs true if user was successfully added to conversation.
    		output.writeObject(writeDataToXML.addUserToConversation(userId, convId));
    		Singletons.getlogsWriter().write("user added to conversation.");
    	} catch (IOException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
    /**
     * Reads a userId, then outputs corresponding user's list of conversations' names to the stream.
     * @throws IOException 
     */
    public void getUserConvNameList() throws IOException {
    	try {
    		UUID userId = (UUID)input.readObject();
    		Utilisateur u = getDataFromXML.getUserById(userId);
    		List<String> convNames = getDataFromXML.getConvNamesOfUser(u);
    		output.writeObject(convNames);
    	} catch (IOException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
    /**
     * Reads a conversation ID, then outputs corresponding conversation object to the stream.
     * @throws IOException 
     */
    public void getSelectedConvById() throws IOException {
    	try {
    		UUID convId = (UUID)input.readObject();
    		Conversation conv = getDataFromXML.getConvById(convId);
    		output.writeObject(conv);
    	} catch (IOException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
    /**
     * Reads a conversation ID, then outputs the names of every user participating in the conversation.
     * @throws IOException 
     */
    public void getUsersNames() throws IOException {
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
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();

    	} catch (ClassNotFoundException ex) {
    		Singletons.getlogsWriter().write("Server exception: " + ex.getMessage()+"\n");
    		PrintWriter pw = new PrintWriter(new File("file.txt"));
    	    ex.printStackTrace(pw);
    	    pw.close();
    	}
    }
}
