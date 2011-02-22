package net.modera.shovel.traveler;

import java.util.ArrayList;
import java.util.Stack;
import java.util.List;

import org.springframework.stereotype.Component;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;

@Component("traveler")
public class ResourceTraveler {
	
	private Stack<Resource> history;
	private Stack<Resource> forwardHistory;
	
	private List<TravelerEventListener> listeners;
	private List<ResourceProvider> connectionProviders;
	
	private Resource currentResource;
	
	public ResourceTraveler() {
		history = new Stack<Resource>();
		forwardHistory = new Stack<Resource>();
		listeners = new ArrayList<TravelerEventListener>();
	}

	public Stack<Resource> getHistory() {
		return history;
	}

	private void setCurrentResource(Resource currentResource) {
		this.currentResource = currentResource;
	}
	
	public void beginWithResource(Resource currentResource) {
		setCurrentResource(currentResource);
		reset();
	}

	public Resource getCurrentResource() {
		return currentResource;
	}

	public void moveWith(Connection con) {
		
		if (!getConnections().contains(con)) {
			throw new UnknownConnectionException();
		}
		
		history.push(getCurrentResource());
		setCurrentResource(con.getTargetResource());
		
		onTravelerMoved();
	}

	public void moveBack() {
		forwardHistory.push(getCurrentResource());
		setCurrentResource(history.pop());
		
		onTravelerMoved();
	}

	public Stack<Resource> getForwardHistory() {
		return forwardHistory;
	}

	public void moveForward() {
		history.push(getCurrentResource());
		setCurrentResource(forwardHistory.pop());
		
		onTravelerMoved();
	}
	
	public void addListener(TravelerEventListener listener) {
		listeners.add(listener);
	}

	public void setListeners(List<TravelerEventListener> listeners) {
		this.listeners = listeners;
	}

	public List<TravelerEventListener> getListeners() {
		return listeners;
	}
	
	protected void reset() {
		history.clear();
		forwardHistory.clear();
		
		for (TravelerEventListener listener : listeners) {
			listener.onReset();
		}
	}
	
	protected void onTravelerMoved() {
		for (TravelerEventListener listener : listeners) {
			listener.onTravelerMoved();
		}
	}

	public void setConnectionProviders(List<ResourceProvider> connectionProviders) {
		this.connectionProviders = connectionProviders;
	}

	public List<ResourceProvider> getConnectionProviders() {
		return connectionProviders;
	}
	
	public List<Connection> getConnections() {
		
		List<Connection> connections = new ArrayList<Connection>();
		
		for (ResourceProvider provider : getConnectionProviders()) {
			connections.addAll(provider.getResourceConnections(getCurrentResource()));
		}
		
		return connections;
	}
}
