import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.document.TextField;
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
import java.nio.file.Files;
import java.nio.file.Paths;

public class LuceneXMLProcessor
{
	
	
	@SuppressWarnings("deprecation")
	//Optional Arguments
	//Argument 1 file directory
	//Argument 2 lucene search query
	public static String[] GO(String directory, String querystr) throws ParseException, IOException 
	{
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
	    Directory index = new RAMDirectory();
	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	    

	    IndexWriter w = new IndexWriter(index, config);

	    Files.walk(Paths.get(directory)).forEach(filePath -> {
	        if (Files.isRegularFile(filePath)) 
	        {
	            LuceneDocumentBuilder.addDoc(w, filePath.toString());
	        }
	    });	 
		    
	    w.close();
	
	    Query q = new QueryParser("", analyzer).parse(querystr);
	    
	    int hitsPerPage = 1000;
	    IndexReader reader = DirectoryReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    String[] results = new String[10];
	    
	    System.out.println("Found " + hits.length + " hits.");
	    
	    for (int i = 0; i < hits.length; ++i) {
	      int docId = hits[i].doc;
	      Document d = searcher.doc(docId);
	      results [i] = d.get("FirstName");
	      System.out.println((i + 1) + ". \t" + d.get("FirstName") + " " + d.get("LastName") + "\t\t" + d.get("EmailAddress"));
	    }

	    reader.close();
	    
	    return results;
	    
	}
}
