import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;


public class Driver {

	public static void main(String[] args) throws ParseException, IOException {
		// TODO Auto-generated method stub
		
		String[] query = {"Book","lucene"};
		
		
		LuceneXMLProcessor.GO(query);
	}

}
