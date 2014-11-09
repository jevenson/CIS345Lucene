import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

public class LuceneXMLProcessor 
{
	public static void main(String[] args) throws ParseException, IOException 
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	    Directory index = new RAMDirectory();
	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	    IndexWriter w = new IndexWriter(index, config);
	    
	    for (int i = 1; i <= 20; i++) {
	    	CustomerDocuments.addDoc(w, ".\\xml\\customers\\customers" + i + ".xml");
	    }
	    
	    w.close();
	    
		String querystr = args.length > 0 ? args[0] : "A";
	
	    Query q = new QueryParser(Version.LUCENE_40, "LastName", analyzer).parse(querystr);
	    
	    CustomerDocuments cDocuments = new CustomerDocuments();   
	    
	    int hitsPerPage = 1000;
	    IndexReader reader = DirectoryReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    System.out.println("Found " + hits.length + " hits.");
	    
	    for (int i = 0; i < hits.length; ++i) {
	      int docId = hits[i].doc;
	      Document d = searcher.doc(docId);
	      
	      System.out.println((i + 1) + ". \t" + d.get("FirstName") + " " + d.get("LastName") + "\t\t" + d.get("EmailAddress"));
	    }

	    reader.close();
	    
	}
}
