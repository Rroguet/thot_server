package presentation.model;

import java.io.Serializable;
import java.util.*;
/**
 * Stores Conversation necessary attributes
 * @author jules
 * 
 */
@SuppressWarnings("serial")
public class Conversation implements Serializable{
	/**
	 * 
	 */
	private UUID convId;
	private List<Message> messages;
	private List<UUID> utilisateurs;
	private String nameConv;
	private UUID createur;

	public Conversation(String nameConv, UUID createur, List<Message> messages, List<UUID> utilisateurs) {
		convId = UUID.randomUUID();
		this.nameConv = nameConv;
		this.createur = createur;
		this.messages = messages;
		this.utilisateurs = utilisateurs;
	}
	public Conversation(UUID convId, String nameConv, UUID createur, List<Message> messages, List<UUID> utilisateurs) {
		this.convId = convId;
		this.nameConv = nameConv;
		this.createur = createur;
		this.messages = messages;
		this.utilisateurs = utilisateurs;
	}
	
	public UUID getConvId() {
		return convId;
	}
	
	public String getName() {
		return nameConv;
	}
	
	public UUID getCreateur() {
		return createur;
	}
	
	public List<Message> getMessage(){
		return messages;
	}
	public List<UUID> getUserList() {
		return utilisateurs;
	}
	
	public void newMessage(String m, Utilisateur u, int idConv) {
		messages.add(new Message(u.getId(),m));
	}
	
}
