package presentation.model;

import java.io.Serializable;

public class Message implements Serializable{
	private int utilisateurId;
	private String message;
	//private Date date;
	
	public Message(int utilisateurId, String message) {
		this.utilisateurId = utilisateurId;
		this.message = message;
	}
	
	public int getUtilisateur() {
		return utilisateurId;
	}
	
	public String getMessage() {
		return message;
	}
}
