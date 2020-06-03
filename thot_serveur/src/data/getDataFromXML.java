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
	
	public NodeList parseXMLFile (String filePath) {
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
	
	public Utilisateur getUtilisateur(String login, String passWord){
	    NodeList nodes = parseXMLFile(Constant.pathUserXML);
	    if (nodes == null) return null;
	    
		for (int i = 0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
				Element currentElement = (Element) nodes.item(i);
	    		if (currentElement.getNodeName().equals("user")){
	    			if(currentElement.getElementsByTagName("login").item(0).getTextContent().equals(login)&&currentElement.getElementsByTagName("passWord").item(0).getTextContent().equals(passWord)) {
	    				try {
	    					List<String> conv = new ArrayList<String>();
	    					NodeList nodeConv = currentElement.getElementsByTagName("conversations").item(0).getChildNodes();
	    					//Element test = (Element) nodeConv.item(0).getChildNodes();
	    					/*for (int j = 0; j<nodeConv.getLength(); j++) {
	    						if (nodeConv.item(j).getNodeType() == Node.ELEMENT_NODE)   {
	    							Element currentConv = (Element) nodeConv.item(i);
	    							conv.add(currentConv.getElementsByTagName("conversation").item(0).getTextContent());
	    						}
	    					}*/
	    					//if(conv.get(0) == null) System.out.println("erreur");
	    					//else System.out.println("num conv = "+conv.get(0));
	    					System.out.println("test conv xml:"+nodeConv.item(3).getTextContent());
	    					Utilisateur u = new Utilisateur(currentElement.getElementsByTagName("firstName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("lastName").item(0).getTextContent(),
	    										currentElement.getElementsByTagName("userName").item(0).getTextContent(),
	    										Integer.parseInt(currentElement.getElementsByTagName("id").item(0).getTextContent()),
	    										null);
	    					return u;
	    				} catch (Exception ex) {}
	    			}
	    		}
	    	}
	    }
	    return null;
	}
}
