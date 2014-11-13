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
public class LuceneDocumentBuilder 
{	
	public static void addDoc(IndexWriter w, String xmlFilePath) 
	{
		try {
			NodeList nodes = XMLParser.parseXMLFile(xmlFilePath);
					 nodes = nodes.item(0).getChildNodes();
			
			for (int i = 0; i < nodes.getLength(); i++) {
				//gets the node at the specified index
				Node firstNode = nodes.item(i);

				if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
					//puts the first customer into an element
					Element firstElement = (Element) firstNode;
					
					Document doc = new Document();
					
					for (int x = 0; x < firstElement.getChildNodes().getLength() - 1; x++) {
						Node currentChildNode = firstElement.getChildNodes().item(x);
						
						if (currentChildNode.getNodeType() == Node.ELEMENT_NODE) {
							doc.add(new TextField(currentChildNode.getNodeName(), getNodeValue(firstElement, currentChildNode.getNodeName()), Field.Store.YES));
						}
					}
					
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
