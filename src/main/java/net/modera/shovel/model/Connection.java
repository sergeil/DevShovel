package net.modera.shovel.model;

public class Connection {
	
	private Resource target;
	
	public Connection(Resource targetResource) {
		target = targetResource;
	}

	public Resource getTargetResource() {
		return target;
	}
	
}
