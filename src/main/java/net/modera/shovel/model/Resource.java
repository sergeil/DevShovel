package net.modera.shovel.model;

import java.util.List;

public abstract class Resource {
	
	private String displayName;
	
	private List<Connection> connections;
	
	public abstract String getResourceKey();
	
	public Resource(String displayName) {
		this.displayName = displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void addConnection(Connection connection) {
		connections.add(connection);
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public List<Connection> getConnections() {
		return connections;
	}
}
