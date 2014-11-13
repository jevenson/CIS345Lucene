import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Field;
//import org.apache.lucene.document.StringField;
//import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
public class LuceneXMLProcessor
{	
	//Map to store key/value pairs of directories and their indexes
	static Map directoryMap = new HashMap();
	
	//Optional Arguments
	//Argument 1 file directory
	//Argument 2 lucene search query
	public static Vector<Map> GO(String directory, String querystr) throws ParseException, IOException 
	{
		//declaring an analyzer and instantiating the index to null
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
		Directory index = null;
		
		//check to see if the index has already been built for this directory
		if (directoryMap.get(directory) == null) {
			//the index has not been built for the provided dictionary
			index = new RAMDirectory();
			
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			IndexWriter w = new IndexWriter(index, config);
			
			//For each file in the directory, run it through the Lucene Document Builder 
			//	build documents and add them to the index writer
			File[] files = new File(directory).listFiles();
		    for (File file : files) {
		        if (!file.isDirectory()) {
		        	LuceneDocumentBuilder.addDoc(w, file.getAbsolutePath().toString());
		        }
		    }
			w.close();
			
			//write documents to index
			directoryMap.put(directory, index);
		} else {
			//Index has already been build for the directory
			index = (Directory) directoryMap.get(directory);
		}
	
		//query parse, no default field specified, hence the ""
	    Query q = new QueryParser("", analyzer).parse(querystr);
	    
	    //Perform the search
	    int hitsPerPage = 1000;
	    IndexReader reader = DirectoryReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    //Build results vector comprised of dictionaries
		Vector<Map> results = new Vector<Map>();
	    
	    
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			
			Map result = new HashMap();
				
			List<IndexableField> fields = d.getFields();
			
			for (int x = 0; x < fields.size() - 1; x++) {
				IndexableField field = fields.get(x);
				
				//Add the document field name and its value to the map (key value pair)
				result.put(field.name(), field.stringValue());
			}
			
			results.add(result);
		}
		
	    reader.close();
	    
	    //Output code, for testing porpoises 
	    System.out.println("Found " + hits.length + " hits.");
	    for (int i = 0; i < results.size() - 1; i++) {
	    	Map result = results.get(i);
	    	
	    	Iterator it = result.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry pairs = (Map.Entry)it.next();
	            System.out.println(padRight(pairs.getKey().toString(), 20) + " " + padRight(pairs.getValue().toString(), 20));
	        }
	
	    	System.out.println();
	    } //end output 
	    
	    //return the vector of map results
	    return results;
	}
	
	private static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
}
