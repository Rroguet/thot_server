package presentation.model;

import java.io.Serializable;
import java.util.*;
/**
 * Stores information of a user.
 * @author jules
 * 
 */
@SuppressWarnings("serial")
public class Utilisateur implements Serializable{
	private String firstName;
	private String lastName;
	private String userName;
	private UUID id;
	private List<UUID> conversationsId;
	
	public Utilisateur (String firstName, String lastName, String userName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.id = UUID.randomUUID();
		conversationsId = new ArrayList<UUID>();
	}
	public Utilisateur (String firstName, String lastName, String userName, UUID userId, List<UUID> conversationsId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		id = userId;
		this.conversationsId = conversationsId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
	}
	
	public UUID getId() {
		return id;
	}
	
	public List<UUID> getConversationList(){
		return conversationsId;
	}
}