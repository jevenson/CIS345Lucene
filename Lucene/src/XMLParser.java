//Jon Meer & Josh Evenson
//CIS 345 Final Project
//11-30-2014
//For Dr. Weimin He

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser 
{
	//Accepts a file path and parses the XML file, returning a NodeList
	public static NodeList parseXMLFile(String filePath) throws ParserConfigurationException, SAXException, IOException 
	{	
		File file = new File(filePath);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		
		doc.getDocumentElement().normalize();
		
		return doc.getChildNodes();
	}
}
