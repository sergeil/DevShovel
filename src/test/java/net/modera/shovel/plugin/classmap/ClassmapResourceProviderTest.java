package net.modera.shovel.plugin.classmap;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassmapResourceProviderTest {
	
	@Test
	public void testInterfaceInheritance() {
		
		ClassmapResourceProvider provider = new ClassmapResourceProvider();
		
		List<String> interfaces = new ArrayList<String>();
		interfaces.add("Interface\\Fauna");
		provider.addClass("Animal", interfaces, null);
		provider.addClass("Dog", null, "Animal");
		
		List<Resource>  resources = provider.getResources();
		assertEquals(3, resources.size());
		
		List<Connection> connections = provider.getResourceConnections(
					new Resource("Dog")
				);
		assertEquals(2, connections.size());
	}
}
