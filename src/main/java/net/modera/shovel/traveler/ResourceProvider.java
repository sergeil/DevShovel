package net.modera.shovel.traveler;

import net.modera.shovel.model.*;
import java.util.List;

public interface ResourceProvider {
	
	public List<Resource> findResources(ResourceFindCriteria criteria);
	
	public List<Connection> getConnectionsForResource(Resource resource); 
}
