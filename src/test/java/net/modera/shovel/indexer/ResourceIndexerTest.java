package net.modera.shovel.indexer;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;
import net.modera.shovel.model.*;

public class ResourceIndexerTest {
	
	@Test
	public void testSearch() throws ParseException {
		
		ResourceIndexer indexer = new ResourceIndexer();
		
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource("Foo bar baz"));
		resources.add(new Resource("bar baz"));
		resources.add(new Resource("Foo baz"));
		
		indexer.index(resources);
		
		List<Resource> foundResources = indexer.search("foo");  
		assertNotNull(foundResources);
		assertEquals(2, foundResources.size());
	}
	
	@Test
	public void testEmptySearch() throws ParseException {
		
		ResourceIndexer indexer = new ResourceIndexer();
		
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(new Resource("Foo bar baz"));
		resources.add(new Resource("bar baz"));
		resources.add(new Resource("Foo baz"));
		
		indexer.index(resources);
		
		List<Resource> foundResources = indexer.search("");  
		assertNotNull(foundResources);
		assertEquals(0, foundResources.size());
	}
}
