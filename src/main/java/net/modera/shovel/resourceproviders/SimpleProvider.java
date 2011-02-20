package net.modera.shovel.resourceproviders;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import net.modera.shovel.traveler.ResourceFindCriteria;

import org.springframework.stereotype.Component;

@Component
public class SimpleProvider implements DiscoverableResourceProvider {
	List<Resource> resources;
	
	public SimpleProvider() {
		resources = new ArrayList<Resource>();
		resources.add(new Resource("MyController"));
	}
	
	public List<Connection> getConnectionsForResource(Resource resource) {
		List<Connection> connections = new ArrayList<Connection>();
		
		if (resource == resources.get(0)) {
			connections.add(new Connection(new Resource("list action")));
			connections.add(new Connection(new Resource("index action")));
		}
		
		return connections;
	}

	public List<Resource> findResources(ResourceFindCriteria criteria) {
		return resources;
	}

}
