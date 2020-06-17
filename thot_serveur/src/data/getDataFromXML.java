package data;

import presentation.model.*;


import javax.xml.parsers.*;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
import java.io.IOException;
import java.io.File;
import java.util.*;

public class getDataFromXML {
	
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	
	public getDataFromXML() {
		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
	}
	
	public static NodeList parseXMLFile (String filePath) {
		NodeList elementNodes = null;
		try {
			Document document= documentBuilder.parse(new File(filePath));
			Element root = document.getDocumentElement();

			elementNodes = root.getChildNodes();
		} catch (SAXException e){
		} catch (IOException e){
		}
		return elementNodes;
	}
	
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
	    										currentElement.getElementsByTagName("userName").item(0).getTextContent(),
	    										Integer.parseInt(currentElement.getElementsByTagName("id").item(0).getTextContent()),
	    										conv);
	    					return u;
	    				} catch (Exception ex) {}
	    			}
	    		}
	    	}
	    }
	    return null;
	}
	
	public static Utilisateur getUserById(int userId){
	    NodeList nodes = parseXMLFile(Constant.pathUserXML);
	    if (nodes == null) return null;
	    
		for (int i = 0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element currentElement = (Element) nodes.item(i);
	    		if (currentElement.getNodeName().equals("user")){
	    			if(Integer.parseInt(currentElement.getElementsByTagName("id").item(0).getTextContent()) == userId) {
	    				try {
	    					List<Integer> conv = new ArrayList<Integer>();
	    					NodeList nodeConv = currentElement.getElementsByTagName("conversations").item(0).getChildNodes();
	    					for (int j = 1; j<nodeConv.getLength(); j+=2) {
	    						conv.add(Integer.parseInt(nodeConv.item(j).getTextContent()));
	    					}
	    					Utilisateur u = new Utilisateur(currentElement.getElementsByTagName("firstName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("lastName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("userName").item(0).getTextContent(),
	    										Integer.parseInt(currentElement.getElementsByTagName("id").item(0).getTextContent()),
	    										conv);
	    					return u;
	    				} catch (Exception ex) {}
	    			}
	    		}
	    	}
	    }
	    return null;
	}
	
	public static Conversation getConvById(int convId) {
		List<Utilisateur> userList;
		List<Message> msgList;
		NodeList nodes = parseXMLFile(Constant.pathConvXML);
		if (nodes == null) return null;

		for (int i = 0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element currentElement = (Element) nodes.item(i);
	    		if (currentElement.getNodeName().equals("conv")){
	    			Conversation conv;
	    			if(Integer.parseInt(currentElement.getElementsByTagName("idConv").item(0).getTextContent())==convId) {
	    				try {
	    					
	    					msgList = new ArrayList<Message>();
	    					userList = new ArrayList<Utilisateur>();
	    					//Block filling the message list of the conversation
	    	                NodeList nodeMsg = currentElement.getElementsByTagName("messages").item(0).getChildNodes();
	    	                for (int j=0;j<nodeMsg.getLength();j++) {
	    	                	Node node = nodeMsg.item(j);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                	Element message = (Element) node;
	    	                        String utilisateur = message.getElementsByTagName("utilisateur").item(0).getTextContent();
	    	                        String text = message.getElementsByTagName("text").item(0).getTextContent();
	    	                        Message msg = new Message(Integer.parseInt(utilisateur), text);
	    	                        msgList.add(msg);
	    	                    }
	    	                } 

	    					NodeList nodeUserIds = currentElement.getElementsByTagName("utilisateurs").item(0).getChildNodes();
	    					for (int j=0;j<nodeUserIds.getLength();j++) {
	    						Node node = nodeUserIds.item(j);
	    						if (node.getNodeType() == Node.ELEMENT_NODE) {
	    							Element user = (Element) node;
	    							String userTagContent = (user.getTextContent());
	    							int userId = Integer.parseInt(userTagContent);
	    							userList.add(getUserById(userId));
	    						}
	    					}
	    					conv = new Conversation(convId,
	    										currentElement.getElementsByTagName("nameConv").item(0).getTextContent(),
	    										Integer.parseInt(currentElement.getElementsByTagName("idCreateur").item(0).getTextContent()),
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
	
	public static List<Conversation> getConvListOfUser(Utilisateur u){
		List<Conversation> convList = new ArrayList<Conversation>();
		for (Integer i : u.getConversationList()) {
			convList.add(getConvById(i));
		}
		return convList;
	}
	
	public static List<String> getConvNamesOfUser(Utilisateur u){
		List<String> convNames = new ArrayList<String>();
		for (Integer i : u.getConversationList()) {
			convNames.add(getConvById(i).getName());
		}
		return convNames;
	}
	
	public static List<Utilisateur> getUserListFromIdList(List<Integer> userIdList){
		List<Utilisateur> userList = new ArrayList<Utilisateur>();
		for (Integer i : userIdList) {
			userList.add(getUserById(i));
		}
		return userList;
	}
}
