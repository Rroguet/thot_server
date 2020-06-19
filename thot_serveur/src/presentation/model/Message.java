package presentation.model;

import java.io.Serializable;
import java.util.UUID;
/**
 * Message
 * @author jules
 * 
 */
@SuppressWarnings("serial")
public class Message implements Serializable{
	private UUID utilisateurId;
	private String message;
	//private Date date;
	
	public Message(UUID utilisateurId, String message) {
		this.utilisateurId = utilisateurId;
		this.message = message;
	}
	
	public UUID getUtilisateur() {
		return utilisateurId;
	}
	
	public String getMessage() {
		return message;
	}
}
