package net.modera.shovel.provider;

import java.io.IOException;

import net.modera.shovel.traveler.ResourceProvider;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;


public class LucineProviderTest {

	@Test
	public void testFindResources() throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {
		ResourceProvider provider = new LucineProvider();
		
		System.out.println(provider.getResources("Lucene"));
	}
}
