package net.modera.shovel.plugin.classmap;

import java.io.File;
import java.util.List;

import net.modera.shovel.model.Resource;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassmapResourceProviderTest {
	
	@Test
	public void testGetResources() {
		File file = new File(ClassLoader.getSystemResource("shovel_dump.json").getFile());
		
		ClassmapResourceProvider provider = new ClassmapResourceProvider();
		provider.setDumpFile(file);
		List<Resource>  resources = provider.getResources();
		
		assertEquals(820, resources.size());
	}
}
