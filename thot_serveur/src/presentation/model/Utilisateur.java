package presentation.model;

import java.io.Serializable;
import java.util.*;

public class Utilisateur implements Serializable{
	private String firstName;
	private String lastName;
	private String userName;
	private int id;
	private List<Integer> conversationsId;
	
	public Utilisateur (String firstName, String lastName, String userName, int id, List<Integer> conversationsId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.id = id;
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
	
	public int getId() {
		return id;
	}
	
	public List<Integer> getConversationList(){
		return conversationsId;
	}
}
