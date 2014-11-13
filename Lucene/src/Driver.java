import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;


public class Driver {

	//Used for testing in Java, before building into JAR & DLL for use in winforms app
	public static void main(String[] args) throws ParseException, IOException 
	{
		//first call for customer search, index is not build. search will be slow
		LuceneXMLProcessor.GO(".//xml//customers", "LastName:\"Holcomb\"");
		
		//first call for book search, index is not build. search will be slow
		LuceneXMLProcessor.GO(".//xml//books", "Author:\"Ulla\"");
		
		//second call for customer search, index is not build. search will be fast
		LuceneXMLProcessor.GO(".//xml//customers", "LastName:\"Anderson\"");
		
		//second call for book search, index is not build. search will be fast
		LuceneXMLProcessor.GO(".//xml//books", "Author:\"Gay\"");
	}

}
