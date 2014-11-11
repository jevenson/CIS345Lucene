import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;


public class Driver {

	public static void main(String[] args) throws ParseException, IOException {
		

		LuceneXMLProcessor.GO(".//xml//customers", "LastName:\"Holcomb\"");

		
	}

}
