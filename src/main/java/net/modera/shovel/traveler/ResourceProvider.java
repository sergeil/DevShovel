package net.modera.shovel.traveler;

import net.modera.shovel.model.*;

import java.util.List;

public interface ResourceProvider {
	
	public List<Resource> getResources();
	
	public List<Connection> getResourceConnections(Resource resource);
}
