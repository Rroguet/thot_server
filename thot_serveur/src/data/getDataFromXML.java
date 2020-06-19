package data;

import presentation.model.*;
import singletons.Singletons;

import org.xml.sax.SAXException;

import org.w3c.dom.*;
import java.io.IOException;
import java.io.File;
import java.util.*;

/**
 * This class is used to read data from xml files.
 * @author jules
 * 
 */
public class getDataFromXML {
	/**
	 * 	
	 * @param filePath path of file to parse
	 * @return
	 * Parses the xml file given in parameter for further use.
	 */
	public static NodeList parseXMLFile (String filePath) {
		NodeList elementNodes = null;
		try {
			Document document= Singletons.getDocumentBuilder().parse(new File(filePath));
			Element root = document.getDocumentElement();

			elementNodes = root.getChildNodes();
		} catch (SAXException e){
		} catch (IOException e){
		}
		return elementNodes;
	}
	/**
	 * 
	 * @param login login entered by user
	 * @param passWord password entered by user
	 * @return Utilisateur
	 * Returns the user from XML file which corresponds to the given login and password 
	 */
	public static Utilisateur getUtilisateur(String login, String passWord){
	    NodeList nodes = parseXMLFile(Constant.pathUserXML);
	    if (nodes == null) return null;
	    
		for (int i = 0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element currentElement = (Element) nodes.item(i);
	    		if (currentElement.getNodeName().equals("user")){
	    			if(currentElement.getElementsByTagName("login").item(0).getTextContent().equals(login)&&currentElement.getElementsByTagName("passWord").item(0).getTextContent().equals(passWord)) {
	    				try {
	    					List<Integer> conv = new ArrayList<Integer>();
	    					NodeList nodeConv = currentElement.getElementsByTagName("conversations").item(0).getChildNodes();
	    					for (int j = 1; j<nodeConv.getLength(); j+=2) {
	    						conv.add(Integer.parseInt(nodeConv.item(j).getTextContent()));
	    					}
	    					Utilisateur u = new Utilisateur(currentElement.getElementsByTagName("firstName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("lastName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("userName").item(0).getTextContent());
	    					return u;
	    				} catch (Exception ex) {}
	    			}
	    		}
	    	}
	    }
	    return null;
	}
	/**
	 * 
	 * @param userId UUID of the user we want to get from XML file.
	 * @return Utilisateur
	 * Returns the user corresponding to a given UUID, if there is no such user, returns null
	 */
	public static Utilisateur getUserById(UUID userId){
	    NodeList nodes = parseXMLFile(Constant.pathUserXML);
	    if (nodes == null) return null;
	    
		for (int i = 0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element currentElement = (Element) nodes.item(i);
	    		if (currentElement.getNodeName().equals("user")){
	    			if(UUID.fromString(currentElement.getElementsByTagName("id").item(0).getTextContent()) == userId) {
	    				try {
	    					List<UUID> conv = new ArrayList<UUID>();
	    					NodeList nodeConv = currentElement.getElementsByTagName("conversations").item(0).getChildNodes();
	    					for (int j = 1; j<nodeConv.getLength(); j+=2) {
	    						conv.add(UUID.fromString(nodeConv.item(j).getTextContent()));
	    					}
	    					Utilisateur u = new Utilisateur(currentElement.getElementsByTagName("firstName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("lastName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("userName").item(0).getTextContent(),
	    										userId,
	    										conv);
	    					return u;
	    				} catch (Exception ex) {}
	    			}
	    		}
	    	}
	    }
	    return null;
	}
	/**
	 * 
	 * @param convId UUID of the conversation we want to read from XML.
	 * @return Conversation
	 * Returns the conversation corresponding to the given UUID, if there is no such conversation, returns null.
	 */
	public static Conversation getConvById(UUID convId) {
		List<Utilisateur> userList;
		List<Message> msgList;
		NodeList nodes = parseXMLFile(Constant.pathConvXML);
		if (nodes == null) return null;

		for (int i = 0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element currentElement = (Element) nodes.item(i);
	    		if (currentElement.getNodeName().equals("conv")){
	    			Conversation conv;
	    			if(UUID.fromString(currentElement.getElementsByTagName("idConv").item(0).getTextContent())==convId) {
	    				try {
	    					
	    					msgList = new ArrayList<Message>();
	    					userList = new ArrayList<Utilisateur>();
	    					//Block filling the message list of the conversation
	    	                NodeList nodeMsg = currentElement.getElementsByTagName("messages").item(0).getChildNodes();
	    	                for (int j=0;j<nodeMsg.getLength();j++) {
	    	                	Node node = nodeMsg.item(j);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                	Element message = (Element) node;
	    	                        String utilisateurId = message.getElementsByTagName("utilisateur").item(0).getTextContent();
	    	                        String text = message.getElementsByTagName("text").item(0).getTextContent();
	    	                        Message msg = new Message(UUID.fromString(utilisateurId), text);
	    	                        msgList.add(msg);
	    	                    }
	    	                } 

	    					NodeList nodeUserIds = currentElement.getElementsByTagName("utilisateurs").item(0).getChildNodes();
	    					for (int j=0;j<nodeUserIds.getLength();j++) {
	    						Node node = nodeUserIds.item(j);
	    						if (node.getNodeType() == Node.ELEMENT_NODE) {
	    							Element user = (Element) node;
	    							String userTagContent = (user.getTextContent());
	    							UUID userId = UUID.fromString(userTagContent);
	    							userList.add(getUserById(userId));
	    						}
	    					}
	    					conv = new Conversation(convId,
	    										currentElement.getElementsByTagName("nameConv").item(0).getTextContent(),
	    										UUID.fromString(currentElement.getElementsByTagName("idCreateur").item(0).getTextContent()),
	    										msgList,
	    										userList);
	    					return conv;
	    				} catch (Exception ex) {}
	    			}
	    		}
	    	}
	    }
	    return null;
	}
	/**
	 * 
	 * @param u user object from which we want the conversation list.
	 * @return List of conversation
	 * For a given user, return the list of conversations he participates in.
	 */
	public static List<Conversation> getConvListOfUser(Utilisateur u){
		List<Conversation> convList = new ArrayList<Conversation>();
		for (UUID i : u.getConversationList()) {
			convList.add(getConvById(i));
		}
		return convList;
	}
	/**
	 * 
	 * @param u user object from which we want the names of conversations he participates in.
	 * @return List of names
	 * For a given user, return the list of names of the conversations he participates in.
	 */
	public static List<String> getConvNamesOfUser(Utilisateur u){
		List<String> convNames = new ArrayList<String>();
		for (UUID i : u.getConversationList()) {
			convNames.add(getConvById(i).getName());
		}
		return convNames;
	}
	/**
	 * 
	 * @param userIdList ID list that we use to find corresponding users.
	 * @return List of users
	 * For a given UUID list, returns the corresponding list of users.
	 */
	public static List<Utilisateur> getUserListFromIdList(List<UUID> userIdList){
		List<Utilisateur> userList = new ArrayList<Utilisateur>();
		for (UUID i : userIdList) {
			userList.add(getUserById(i));
		}
		return userList;
	}
}
