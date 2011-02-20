package net.modera.shovel.resourceproviders;

import net.modera.shovel.model.*;
import net.modera.shovel.traveler.ResourceFindCriteria;

import java.util.List;

public interface ResourceProvider {
	
	public List<Resource> findResources(ResourceFindCriteria criteria);
	
	/**
	 * @param resource
	 * @return All connections that an implementation is able to provide
	 */
	public List<Connection> getConnectionsForResource(Resource resource); 
}
