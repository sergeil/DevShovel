package net.modera.shovel.model;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class Resource {
	
	private String displayName;
	
	private List<Connection> connections;
	
	public String getResourceKey() {
		return "generic";
	}
	
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
	
	public void buildDocument(Document doc) {
		doc.add(new Field("name", getDisplayName(), Field.Store.YES, Field.Index.ANALYZED));
	}
	
	public Resource(Document doc) {
		displayName = doc.get("name");
	}
}
