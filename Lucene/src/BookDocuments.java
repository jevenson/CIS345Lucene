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


@SuppressWarnings("unused")
public class BookDocuments 
{	
	public static void addDoc(IndexWriter w, String xmlFilePath) 
	{
		try {
			NodeList nodes = XMLParser.parseXMLFile(xmlFilePath, "book");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				//gets the first customer
				Node firstNode = nodes.item(i);

				if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
					//puts the first customer into an element
					Element firstElement = (Element) firstNode;
					
					Document doc = new Document();
					
					doc.add(new TextField("Title", getNodeValue(firstElement, "Title"), Field.Store.YES));
					doc.add(new TextField("Author", getNodeValue(firstElement, "Author"), Field.Store.YES));
					doc.add(new TextField("PublishDate", getNodeValue(firstElement, "PublishDate"), Field.Store.YES));
					doc.add(new TextField("Price", getNodeValue(firstElement, "Price"), Field.Store.YES));
					
					w.addDocument(doc);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getNodeValue(Element firstElement, String tagName)
	{
		//gets the FirstName child element of customer
		NodeList elementList = firstElement.getElementsByTagName(tagName);
		Element element = (Element) elementList.item(0);
		//gets the child nodes of first name (text is child node)
		NodeList node = element.getChildNodes();
		//add the first name field to the document
		return ((Node) node.item(0)).getNodeValue();
	}
}
