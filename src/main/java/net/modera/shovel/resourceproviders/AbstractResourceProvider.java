package net.modera.shovel.resourceproviders;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import net.modera.shovel.traveler.ResourceFindCriteria;

/**
 * This class is going to be useful when you need to implement
 * only one of the interface's methods. 
 * 
 * @author Sergei Lissovski <sergei.lissovski@gmail.com>
 */
public class AbstractResourceProvider implements ResourceProvider {
	
	public List<Resource> findResources(ResourceFindCriteria criteria) {
		return new ArrayList<Resource>();
	}

	public List<Connection> getConnectionsForResource(Resource resource) {
		return new ArrayList<Connection>();
	}

}
