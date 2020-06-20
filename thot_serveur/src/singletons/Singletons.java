package singletons;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import data.Constant;
/**
 * Design patter singleton.
 * Allows to avoid creating many instances of an object if we only need one.
 * @author jules
 * 
 */
public class Singletons {
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	private static FileWriter logsWriter;
	private static File logFile;
	public static DocumentBuilderFactory getDocumentBuilderFactory() {
		if(documentFactory != null) return documentFactory;
		documentFactory = DocumentBuilderFactory.newInstance();
		return documentFactory;
	}
	/**
	 * Returns the log file.
	 * @return File
	 * @throws FileNotFoundException
	 */
	public static File getlogsFile() throws FileNotFoundException {
		if (logFile != null) return logFile;
		logFile = new File(Constant.logPath);
		return logFile;
	}
	/**
	 * Returns a file writer, so we can print error stack traces into log file.
	 * @return FileWriter
	 * @throws FileNotFoundException
	 */
	public static FileWriter getlogsWriter() throws FileNotFoundException {
		if (logsWriter != null) return logsWriter;
		try {
			logsWriter = new FileWriter(Constant.logPath);
		} catch (IOException e) {
			PrintWriter pw = new PrintWriter(new File(Constant.logPath));
		    e.printStackTrace(pw);
		    pw.close();
		}
		return logsWriter;
	}
	public static DocumentBuilder getDocumentBuilder() throws FileNotFoundException {
		if(documentBuilder != null) return documentBuilder;
		try { 
			documentBuilder = getDocumentBuilderFactory().newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			PrintWriter pw = new PrintWriter(new File(Constant.logPath));
		    pce.printStackTrace(pw);
		    pw.close();
        }
		return documentBuilder;
	}

}
