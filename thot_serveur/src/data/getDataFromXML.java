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
}
