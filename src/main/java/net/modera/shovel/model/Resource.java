package net.modera.shovel.model;

import java.util.ArrayList;
import java.util.List;

public class Resource {

	private String displayName;
	
	private List<Connection> connections = new ArrayList<Connection>();
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void connect(Resource target) {
		addConnection(new Connection(target));
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
