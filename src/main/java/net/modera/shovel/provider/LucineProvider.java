package net.modera.shovel.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import net.modera.shovel.traveler.ResourceProvider;

public class LucineProvider implements ResourceProvider {

	private Directory index;
	private Analyzer analyzer;

	public LucineProvider() {
		createIndex();
	}

	public Analyzer getAnalyzer() {
		if (analyzer == null) {
			analyzer = new StandardAnalyzer(Version.LUCENE_30); 
		}
		return analyzer;
	}

	public void createIndex() {
		index = new RAMDirectory();
		
        // To store an index on disk, use this instead (note that the
        // parameter true will overwrite the index in that directory
        // if one exists):
        // Directory directory = FSDirectory.open(new File("/tmp/testindex"));


		IndexWriter w;
		try {
			w = new IndexWriter(index, getAnalyzer(), true,
					IndexWriter.MaxFieldLength.UNLIMITED);
			addDoc(w, "Lucene in Action");
			addDoc(w, "Lucene for Dummies");
			addDoc(w, "Managing Gigabytes");
			addDoc(w, "The Art of Computer Science");
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

	public List<Resource> getResources(String queryString) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);

        // Now search the index:
        IndexSearcher isearcher = new IndexSearcher(index);

        // Parse a simple query that searches for "text":
        QueryParser parser = new QueryParser(Version.LUCENE_30, "title", analyzer);
        Query query = parser.parse(queryString);

        TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
        isearcher.search(query, collector);

//        System.out.println("collector.getTotalHits()=" + collector.getTotalHits());
//        assertEquals(2, collector.getTotalHits());

        // Iterate through the results:
        
        List<Resource> resources = new ArrayList<Resource>();
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            resources.add(new Resource(hitDoc.get("title")));
        }

        isearcher.close();
        
        return resources;
	}

	public List<Connection> getResourceConnections(Resource resource) {
		List<Connection> connections = new ArrayList<Connection>();

		connections.add(new Connection(new Resource("list action")));
		connections.add(new Connection(new Resource("index action")));

		return connections;
	}

	private void addDoc(IndexWriter w, String value) throws IOException {
		Document doc = new Document();
		doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
		w.addDocument(doc);
	}

}
