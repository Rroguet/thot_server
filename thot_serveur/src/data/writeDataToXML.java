package data;

import presentation.model.*;


import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
import java.io.IOException;
import java.io.File;


public class writeDataToXML {
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	
	public static void initWriteDataToXML() {
		try {
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
	}
	
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
	
	public static void newMessage(Message m, int idConv) {
	    try{
	      Document document = documentBuilder.parse(new File(Constant.pathConvXML));
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
	
	public static void newUser(Utilisateur u, String login, String passWord) {
		try{
		      Document document = documentBuilder.parse(new File(Constant.pathUserXML));
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
		      return;

		    }catch (SAXException e){
		    }catch (IOException e){
		    }
	}
	
	
	public static void addUserToConversationUserXML(int userId, int convId) {
		try{
			Document document = documentBuilder.parse(new File(Constant.pathUserXML));
		    Element root = document.getDocumentElement();
		      
		    NodeList nodes = root.getChildNodes();
		    if (nodes == null) return;
		     
		    for (int i = 0; i<nodes.getLength(); i++) {
		    	if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
		    		Element currentElement = (Element) nodes.item(i);
		            if(currentElement.getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(userId))){
		            	Element conversation = document.createElement("conversation");
		            	conversation.appendChild(document.createTextNode(String.valueOf(convId)));
		              	
		      	      	currentElement.getElementsByTagName("conversations").item(0).appendChild(conversation);
		      	      
		      	      	createXMLFile(document,Constant.pathUserXML);
		      	      	return;
		            }
		          }
		      }

		    }catch (SAXException e){
		    }catch (IOException e){
		    }
	}
	
	public static void addUserToConversationConvXML(int userId, int convId) {
		try{
			Document document = documentBuilder.parse(new File(Constant.pathConvXML));
		    Element root = document.getDocumentElement();
		      
		    NodeList nodes = root.getChildNodes();
		    if (nodes == null) return;
		     
		    for (int i = 0; i<nodes.getLength(); i++) {
		    	if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
		    		Element currentElement = (Element) nodes.item(i);
		            if(currentElement.getElementsByTagName("idConv").item(0).getTextContent().equals(String.valueOf(convId))){
		            	Element utilisateur = document.createElement("utilisateur");
		            	utilisateur.appendChild(document.createTextNode(String.valueOf(userId)));
		            	
		      	      	currentElement.getElementsByTagName("utilisateurs").item(0).appendChild(utilisateur);
		      	      
		      	      	createXMLFile(document,Constant.pathConvXML);
		      	      	return;
		            }
		          }
		      }

		    }catch (SAXException e){
		    }catch (IOException e){
		    }
	}
	
	
	public static void addUserToConversation(int userId, int convId) {
		addUserToConversationUserXML(userId,convId);
		addUserToConversationConvXML(userId,convId);
	}
	
	public static void newConv(Conversation c) {
		try{
		      Document document = documentBuilder.parse(new File(Constant.pathConvXML));
		      Element root = document.getDocumentElement();
		      		      
		      Element conv = document.createElement("conv");

		      Element idConv = document.createElement("idConv");
		      idConv.appendChild(document.createTextNode(String.valueOf(c.getConvId())));
		      conv.appendChild(idConv);

		      Element nameConv = document.createElement("nameConv");
		      nameConv.appendChild(document.createTextNode(c.getNameConv()));
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
