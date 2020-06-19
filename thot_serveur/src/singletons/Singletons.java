package singletons;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
/**
 * Design patter singleton.
 * Allows to avoid creating many instances of an object if we only need one.
 * @author jules
 * 
 */
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
