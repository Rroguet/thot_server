package data;

import presentation.model.*;
import singletons.Singletons;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
import java.io.IOException;
import java.util.UUID;
import java.io.File;

/**
 * This class is used to write data in XML files.
 * It performs actions such as writing newly creating users or conversations, and adding sent messages to conversationXML.
 * @author jules
 * 
 */
public class writeDataToXML {
	
	public static void createXMLFile(Document document, String filePath){
		try {
			DOMSource source = new DOMSource(document);
    	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    	    Transformer transformer = transformerFactory.newTransformer();
    	    StreamResult result = new StreamResult(new File(filePath));
    	    transformer.transform(source, result);
		
		} catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
	}
	/**
	 * 
	 * @param m Message we want to write in XML file.
	 * @param idConv
	 * Adds a message to convXML after it's been sent.
	 */
	public static void newMessage(Message m, int idConv) {
	    try{
	      Document document = Singletons.getDocumentBuilder().parse(new File(Constant.pathConvXML));
	      Element root = document.getDocumentElement();
	      
	      NodeList nodes = root.getChildNodes();
	      if (nodes == null) return;
	      
	      for (int i = 0; i<nodes.getLength(); i++) {
	          if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
	            Element currentElement = (Element) nodes.item(i);
	            if(currentElement.getElementsByTagName("idConv").item(0).getTextContent().equals(String.valueOf(idConv))){
	            	Element message = document.createElement("message");

	            	Element userMessage = document.createElement("utilisateur");
	            	userMessage.appendChild(document.createTextNode(String.valueOf(m.getUtilisateur())));
	            	message.appendChild(userMessage);

	            	Element textMessage = document.createElement("text");
	            	textMessage.appendChild(document.createTextNode(m.getMessage()));
	            	message.appendChild(textMessage);

	            	currentElement.getElementsByTagName("messages").item(0).appendChild(message);
	      	      
	            	createXMLFile(document,Constant.pathConvXML);
	            	return;
	            }
	          }
	      }

	    }catch (SAXException e){
	    }catch (IOException e){
	    }
	}
	/**
	 * 
	 * @param u User object we want to add to XML.
	 * @param login login for the new user.
	 * @param passWord password for the new user.
	 * Adds a newly created user to userXML.
	 * Tests if such a user already exists before writing in XML.
	 */
	public static boolean newUser(Utilisateur u, String login, String passWord) {
		//If username is already used, return false.
		//If password is already used, return false.
		if(getDataFromXML.getUserByUname(u.getUserName())!= null) return false;
		if(getDataFromXML.checkForPassWord(passWord)) return false;
		try{
		      Document document = Singletons.getDocumentBuilder().parse(new File(Constant.pathUserXML));
		      Element root = document.getDocumentElement();
		      		      
		      Element utilisateur = document.createElement("user");

		      Element userLogin = document.createElement("login");
		      userLogin.appendChild(document.createTextNode(login));
		      utilisateur.appendChild(userLogin);

		      Element userPassWord = document.createElement("passWord");
		      userPassWord.appendChild(document.createTextNode(passWord));
		      utilisateur.appendChild(userPassWord);

		      Element userFirstName = document.createElement("firstName");
		      userFirstName.appendChild(document.createTextNode(u.getFirstName()));
		      utilisateur.appendChild(userFirstName);
		      
		      Element userLastName = document.createElement("lastName");
		      userLastName.appendChild(document.createTextNode(u.getLastName()));
		      utilisateur.appendChild(userLastName);
		      
		      Element userUserName = document.createElement("userName");
		      userUserName.appendChild(document.createTextNode(u.getUserName()));
		      utilisateur.appendChild(userUserName);
		      
		      Element userId = document.createElement("id");
		      userId.appendChild(document.createTextNode(String.valueOf(u.getId())));
		      utilisateur.appendChild(userId);
		      
		      Element userConv = document.createElement("conversations");
		      utilisateur.appendChild(userConv);
		      
		      root.appendChild(utilisateur);
		      	      
		      createXMLFile(document,Constant.pathUserXML);

		    }catch (SAXException e){
		    }catch (IOException e){
		    }
		return true;
	}
	
	/**
	 * 
	 * @param userId ID of the user.
	 * @param convId ID of the conversation we want to add to the user's convList.
	 * In userXML file, adds a specific conversation to the conversation list of a user.
	 */
	public static boolean addConvToUserConvList(UUID userId, UUID convId) {
		//return false if the conversation is already in the user's convList
		if(getDataFromXML.getUserById(userId).getConversationList().contains(convId)) return false;
		try{
			Document document = Singletons.getDocumentBuilder().parse(new File(Constant.pathUserXML));
		    Element root = document.getDocumentElement();
		      
		    NodeList nodes = root.getChildNodes();
		    if (nodes == null) return false;
		     
		    for (int i = 0; i<nodes.getLength(); i++) {
		    	if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
		    		Element currentElement = (Element) nodes.item(i);
		            if(currentElement.getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(userId))){
		            	Element conversation = document.createElement("conversation");
		            	conversation.appendChild(document.createTextNode(String.valueOf(convId)));
		              	
		      	      	currentElement.getElementsByTagName("conversations").item(0).appendChild(conversation);
		      	      
		      	      	createXMLFile(document,Constant.pathUserXML);
		      	      	return true;
		            }
		          }
		      }

		    }catch (SAXException e){
		    }catch (IOException e){
		    }
		return false;
	}
	/**
	 * 
	 * @param userId ID of the user to be added in conversation participants.
	 * @param convId ID of matching conversation.
	 * For a specific conversation, adds a user to it's list of participants in convXML file.
	 */
	public static boolean addUserToConversationUserList(UUID userId, UUID convId) {
		//Return false if the user is already in the conversation's userList
		if(getDataFromXML.getConvById(convId).getUserList().contains(userId)) return false;
		try{
			Document document = Singletons.getDocumentBuilder().parse(new File(Constant.pathConvXML));
		    Element root = document.getDocumentElement();
		      
		    NodeList nodes = root.getChildNodes();
		    if (nodes == null) return false;
		     
		    for (int i = 0; i<nodes.getLength(); i++) {
		    	if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
		    		Element currentElement = (Element) nodes.item(i);
		            if(currentElement.getElementsByTagName("idConv").item(0).getTextContent().equals(String.valueOf(convId))){
		            	Element utilisateur = document.createElement("utilisateur");
		            	utilisateur.appendChild(document.createTextNode(String.valueOf(userId)));
		            	
		      	      	currentElement.getElementsByTagName("utilisateurs").item(0).appendChild(utilisateur);
		      	      
		      	      	createXMLFile(document,Constant.pathConvXML);
		      	      	return true;
		            }
		          }
		      }

		    }catch (SAXException e){
		    }catch (IOException e){
		    }
		return false;
	}
	
	/**
	 * 
	 * @param userId UUID of the user added to the conversation.
	 * @param convId UUID of the conversation.
	 * Performs necessary actions when a user is added to a conversation.
	 * returns false if the user is already in the conversation.
	 */
	public static boolean addUserToConversation(UUID userId, UUID convId) {
		if(!addConvToUserConvList(userId,convId) || !addUserToConversationUserList(userId,convId)) return false;
		return true;
	}
	/**
	 * 
	 * @param c conversation object that will be added to XML.
	 * Adds the conversation in parameter to conversationXML.
	 */
	public static void newConv(Conversation c) {
		try{
		      Document document = Singletons.getDocumentBuilder().parse(new File(Constant.pathConvXML));
		      Element root = document.getDocumentElement();
		      		      
		      Element conv = document.createElement("conv");

		      Element idConv = document.createElement("idConv");
		      idConv.appendChild(document.createTextNode(String.valueOf(c.getConvId())));
		      conv.appendChild(idConv);

		      Element nameConv = document.createElement("nameConv");
		      nameConv.appendChild(document.createTextNode(c.getName()));
		      conv.appendChild(nameConv);

		      Element idCreateur = document.createElement("idCreateur");
		      idCreateur.appendChild(document.createTextNode(String.valueOf(c.getCreateur())));
		      conv.appendChild(idCreateur);
		      
		      Element messages = document.createElement("messages");
		      conv.appendChild(messages);
		      
		      Element utilisateurs = document.createElement("utilisateurs");
		      conv.appendChild(utilisateurs);
		      
		      root.appendChild(conv);
		      	      
		      createXMLFile(document,Constant.pathConvXML);
		      return;

		    }catch (SAXException e){
		    }catch (IOException e){
		    }
	}

}
