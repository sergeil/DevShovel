package net.modera.shovel.ship;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.traveler.ResourceProvider;
import net.modera.shovel.traveler.ResourceTraveler;
import net.modera.shovel.traveler.TravelerEventListener;
import net.modera.shovel.traveler.UnknownConnectionException;
import net.modera.shovel.model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ResourceTravelerTest {

	ResourceTraveler traveler;
	
	@Mock ResourceProvider mockProvider;
	
	@Mock Resource res;
	@Mock Resource res2;
	
	Connection con;
	
	@Before
	public void setUp() throws Exception {
		traveler = new ResourceTraveler();
		List<ResourceProvider> providers 
			= new ArrayList<ResourceProvider>();
		providers.add(mockProvider);
		traveler.setConnectionProviders(providers);
		
		con = new Connection(res2);
		
		List<Connection> connections = new ArrayList<Connection>();
		connections.add(con);
		when(mockProvider.getResourceConnections(res)).thenReturn(connections);
	}

	@After
	public void tearDown() throws Exception {
		traveler = null;
	}
	
	@Test
	public void initialStateTest() {
		// should have a movement history
		assertEquals(0, traveler.getHistory().size());
		assertNull(traveler.getCurrentResource());
	}
	
	@Test
	public void getConnections() {
		
		Resource myRes = mock(Resource.class);
		
		Connection con1 = mock(Connection.class);
		List<Connection> connList1 = new ArrayList<Connection>();
		connList1.add(con1);
		ResourceProvider provider1 = mock(ResourceProvider.class);
		when(provider1.getResourceConnections(myRes)).thenReturn(connList1);
		
		Connection con2 = mock(Connection.class);
		List<Connection> connList2 = new ArrayList<Connection>();
		connList2.add(con2);
		ResourceProvider provider2 = mock(ResourceProvider.class);
		when(provider2.getResourceConnections(myRes)).thenReturn(connList2);
		
		List<ResourceProvider> providers 
			= new ArrayList<ResourceProvider>();
		providers.add(provider1);
		providers.add(provider2);
		
		ResourceTraveler myTraveler = new ResourceTraveler();
		myTraveler.setConnectionProviders(providers);
		
		myTraveler.beginWithResource(myRes);
		
		assertSame(myTraveler.getConnections().get(0), con1);
		assertSame(myTraveler.getConnections().get(1), con2);
	}
	
	@Test
	public void moveTo() {
		
		traveler.beginWithResource(res);
	    traveler.moveWith(con);
	    
	    assertSame(res2, traveler.getCurrentResource());
	    assertEquals(1, traveler.getHistory().size());
	}
	
	@Test
	public void moveBack() {
		
		traveler.beginWithResource(res);
		traveler.moveWith(con);
		traveler.moveBack();
	    
	    assertSame(res, traveler.getCurrentResource());
	    assertEquals(0, traveler.getHistory().size());
	    assertEquals(1, traveler.getForwardHistory().size());
	}
	
	@Test
	public void moveForward() {
		
		traveler.beginWithResource(res);
		traveler.moveWith(con);
		traveler.moveBack();
		traveler.moveForward();
		
		assertSame(res2, traveler.getCurrentResource());
		assertEquals(1, traveler.getHistory().size());
		assertEquals(0, traveler.getForwardHistory().size());
	}
	
	@Test
	public void listeners() {
		assertEquals(0, traveler.getListeners().size());
		
		TravelerEventListener listener = mock(TravelerEventListener.class);
		traveler.addListener(listener);
		assertEquals(1, traveler.getListeners().size());
		assertSame(listener, traveler.getListeners().get(0));
		
		List<TravelerEventListener> listenerList = new ArrayList<TravelerEventListener>();
		traveler.setListeners(listenerList);
		assertSame(listenerList, traveler.getListeners());
	}
	
	@Test
	public void beginWithResource() {
		traveler.beginWithResource(res);
		
		TravelerEventListener listener = mock(TravelerEventListener.class);
		traveler.addListener(listener);
		traveler.beginWithResource(res2);
		
		verify(listener).onReset();
		
		assertSame(res2, traveler.getCurrentResource());
		assertEquals(0, traveler.getHistory().size());
		assertEquals(0, traveler.getForwardHistory().size());
	}
	
	@Test(expected=UnknownConnectionException.class)
	public void transitionToUnknownConnection() {
		traveler.beginWithResource(res);
		traveler.moveWith(mock(Connection.class));
	}
	
	@Test
	public void callListenerOnMoveWith() {
		
		TravelerEventListener listener = mock(TravelerEventListener.class);
		traveler.addListener(listener);
		
		traveler.beginWithResource(res);
		traveler.moveWith(con);
		traveler.moveBack();
		traveler.moveForward();
		
		verify(listener, times(3)).onTravelerMoved();
	}
}
