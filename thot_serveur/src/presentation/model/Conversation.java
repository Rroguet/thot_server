package presentation.model;

import java.io.Serializable;
import java.util.*;

public class Conversation implements Serializable{
	private int convId;
	private List<Message> messages;
	private List<Utilisateur> utilisateurs;
	private String name;
	private int createur;

	public Conversation(int convId, String nameConv, int createur, List<Message> messages, List<Utilisateur> utilisateurs) {
		this.name = nameConv;
		this.convId = convId;
		this.createur = createur;
		this.messages = messages;
		this.utilisateurs = utilisateurs;
	}
	
	public int getConvId() {
		return convId;
	}
	public String getName() {
		return name;
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
