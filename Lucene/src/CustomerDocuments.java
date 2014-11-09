import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class CustomerDocuments 
{	
	public static void addDoc(IndexWriter w, String xmlFilePath) 
	{
		try {
			NodeList nodes = CustomerXMLParser.parseCustomerFile(xmlFilePath);
			
			for (int i = 0; i < nodes.getLength(); i++) {
				//gets the first customer
				Node firstNode = nodes.item(i);
				    
				if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
					//puts the first customer into an element
					Element firstElement = (Element) firstNode;
					
					NodeList elementList = null;
					Element element = null;
					NodeList node = null;
					Document doc = new Document();
					
					//gets the FirstName child element of customer
					elementList = firstElement.getElementsByTagName("FirstName");
					element = (Element) elementList.item(0);
					//gets the child nodes of first name (text is child node)
					node = element.getChildNodes();
					//add the first name field to the document
					doc.add(new TextField("FirstName", ((Node) node.item(0)).getNodeValue(), Field.Store.YES));
				
					//Following code collapsed into single line
					
					doc.add(new TextField("LastName", ((Node) ((Element) firstElement.getElementsByTagName("LastName").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					doc.add(new TextField("EmailAddress", ((Node) ((Element) firstElement.getElementsByTagName("EmailAddress").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					doc.add(new TextField("PhoneNumber", ((Node) ((Element) firstElement.getElementsByTagName("PhoneNumber").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					doc.add(new TextField("AltPhoneNumber", ((Node) ((Element) firstElement.getElementsByTagName("AltPhoneNumber").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					doc.add(new TextField("StreetAddress", ((Node) ((Element) firstElement.getElementsByTagName("StreetAddress").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					doc.add(new TextField("City", ((Node) ((Element) firstElement.getElementsByTagName("City").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					doc.add(new TextField("State", ((Node) ((Element) firstElement.getElementsByTagName("State").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					doc.add(new TextField("ZipCode", ((Node) ((Element) firstElement.getElementsByTagName("ZipCode").item(0)).getChildNodes().item(0)).getNodeValue(), Field.Store.YES));
					w.addDocument(doc);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
