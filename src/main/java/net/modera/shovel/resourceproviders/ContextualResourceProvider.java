package net.modera.shovel.resourceproviders;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import net.modera.shovel.traveler.ResourceFindCriteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * @author Sergei Lissovski <sergei.lissovski@gmail.com>
 * 
 * TODO refactor and introduce a callback-based mechanism for
 * merging results
 */
public class ContextualResourceProvider implements ResourceProvider {
	@Autowired
	protected List<DiscoverableResourceProvider> providers;
	
	public List<Resource> findResources(ResourceFindCriteria criteria) {
		List<Resource> mergedResources = new ArrayList<Resource>();
		for (ResourceProvider provider : providers) {
			for (Resource resource : provider.findResources(criteria)) {
				mergedResources.add(resource);
			}
		}
		
		return mergedResources;
	}

	public List<Connection> getConnectionsForResource(Resource resource) {
		List<Connection> mergedConnections = new ArrayList<Connection>();
		for (ResourceProvider provider : providers) {
			for (Connection connection : provider.getConnectionsForResource(resource)) {
				mergedConnections.add(connection);
			}
		}
		
		return mergedConnections;
	}
	
}
