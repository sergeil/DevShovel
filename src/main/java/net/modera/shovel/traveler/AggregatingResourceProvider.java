package net.modera.shovel.traveler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;

@Component
@Qualifier("main")
public class AggregatingResourceProvider implements ResourceProvider {

	private List<ResourceProvider> providers;
	
	@Autowired
	public void setProviders(List<ResourceProvider> providers) {
		this.providers = providers;
	}

	public List<ResourceProvider> getProviders() {
		return providers;
	}
	
	public List<Resource> getResources() {
		
		List<Resource> resources = new ArrayList<Resource>();
		
		for (ResourceProvider provider : providers) {
			resources.addAll(provider.getResources());
		}
		
		return resources;
	}

	public List<Connection> getResourceConnections(Resource resource) {
		
		List<Connection> connections = new ArrayList<Connection>();
		
		for (ResourceProvider provider : providers) {
			connections.addAll(provider.getResourceConnections(resource));
		}
		
		return connections;
	}
}
