package net.modera.shovel.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import net.modera.shovel.model.Resource;

public class ResourceIndexer {

	private Directory index;
	private Analyzer analyzer;
	
	public ResourceIndexer() {
		index = new RAMDirectory();
	}
	
	public List<Resource> search(String queryString) throws ParseException {
        
		List<Resource> resources = new ArrayList<Resource>();
        
		try {
			IndexSearcher isearcher = new IndexSearcher(index);
			
			// Parse a simple query that searches for "text":
	        QueryParser parser = new QueryParser(Version.LUCENE_30, "name", analyzer);
	        Query query = parser.parse(queryString);

	        TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
	        isearcher.search(query, collector);

//	        System.out.println("collector.getTotalHits()=" + collector.getTotalHits());
//	        assertEquals(2, collector.getTotalHits());

	        ScoreDoc[] hits = collector.topDocs().scoreDocs;
	        for (int i = 0; i < hits.length; i++) {
	            Document hitDoc = isearcher.doc(hits[i].doc);
	            resources.add(new Resource(hitDoc));
	        }

	        isearcher.close();
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        
        return resources;
	}
	
	public Analyzer getAnalyzer() {
		if (analyzer == null) {
			analyzer = new StandardAnalyzer(Version.LUCENE_30); 
		}
		return analyzer;
	}
	
	public void index(List<Resource> resources) {
		IndexWriter w;
		try {
			w = new IndexWriter(index, getAnalyzer(), true,
					IndexWriter.MaxFieldLength.UNLIMITED);
			
			for (Resource resource : resources) {
				Document doc = new Document();
				resource.buildDocument(doc);
				w.addDocument(doc);
			}
			w.close();

		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
