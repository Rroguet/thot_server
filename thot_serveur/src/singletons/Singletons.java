package singletons;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Singletons {
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	
	public static DocumentBuilderFactory getDocumentBuilderFactory() {
		if(documentFactory != null) return documentFactory;
		documentFactory = DocumentBuilderFactory.newInstance();
		return documentFactory;
	}
	
	public static DocumentBuilder getDocumentBuilder() {
		if(documentBuilder != null) return documentBuilder;
		try { 
			documentBuilder = getDocumentBuilderFactory().newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
		return documentBuilder;
	}

}
