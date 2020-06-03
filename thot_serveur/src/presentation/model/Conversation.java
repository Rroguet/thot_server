package presentation.model;

import java.io.Serializable;
import java.util.*;

public class Conversation implements Serializable{
	private int convId;
	private List<Message> messages;
	private List<Utilisateur> utilisateurs;
	private String nameConv;
	private int createur;

	public Conversation(String nameConv, int createur, List<Message> messages, List<Utilisateur> utilisateurs) {
		this.createur = createur;
		this.messages = messages;
		this.utilisateurs = utilisateurs;
	}
	
	
	public int getCreateur() {
		return createur;
	}
	
	public List<Message> getMessage(){
		return messages;
	}
	
	public void addUser(Utilisateur u) {
		utilisateurs.add(u);
	}
	
	public void newMessage(String m, Utilisateur u, int idConv) {
		messages.add(new Message(u.getId(),m));
	}
	
	public void removeUser(Utilisateur u) {
		
	}
	
	public void removeMessage(Message m) {
		
	}
}
