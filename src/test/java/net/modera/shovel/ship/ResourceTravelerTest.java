package net.modera.shovel.ship;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.ship.ResourceTraveler;
import net.modera.shovel.model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTravelerTest {

	ResourceTraveler traveler;
	
	@Before
	public void setUp() throws Exception {
		traveler = new ResourceTraveler();
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
		Resource res = mock(Resource.class);
		List<Connection> cList = new ArrayList<Connection>();
		when(res.getConnections()).thenReturn(cList);
		
		traveler.beginWithResource(res);
		assertSame(cList, res.getConnections());
	}
	
	@Test
	public void moveTo() {
		Resource res = new Resource();
		Resource res2 = new Resource();
		Connection con = new Connection(res2);
		res.addConnection(con);
		
	    traveler.beginWithResource(res);
	    traveler.moveWith(con);
	    
	    assertSame(res2, traveler.getCurrentResource());
	    assertEquals(1, traveler.getHistory().size());
	}
	
	@Test
	public void moveBack() {
		Resource res = new Resource();
		Resource res2 = new Resource();
		Connection con = new Connection(res2);
		res.addConnection(con);
		
		traveler.beginWithResource(res);
		traveler.moveWith(con);
		traveler.moveBack();
	    
	    assertSame(res, traveler.getCurrentResource());
	    assertEquals(0, traveler.getHistory().size());
	    assertEquals(1, traveler.getForwardHistory().size());
	}
	
	@Test
	public void moveForward() {
		Resource res = new Resource();
		Resource res2 = new Resource();
		Connection con = new Connection(res2);
		res.addConnection(con);
		
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
		Resource res = new Resource();
		Resource res2 = new Resource();
		Connection con = new Connection(res2);
		res.addConnection(con);
		
		traveler.beginWithResource(res);
		
		TravelerEventListener listener = mock(TravelerEventListener.class);
		traveler.addListener(listener);
		traveler.beginWithResource(res2);
		
		verify(listener).onReset();
		
		assertSame(res2, traveler.getCurrentResource());
		assertEquals(0, traveler.getHistory().size());
		assertEquals(0, traveler.getForwardHistory().size());
	}
	
	@Test
	public void callListenerOnMoveWith() {
		Resource res = new Resource();
		Resource res2 = new Resource();
		Connection con = new Connection(res2);
		res.addConnection(con);
		
		TravelerEventListener listener = mock(TravelerEventListener.class);
		traveler.addListener(listener);
		
		traveler.beginWithResource(res);
		traveler.moveWith(con);
		traveler.moveBack();
		traveler.moveForward();
		
		verify(listener, times(3)).onTravelerMoved();
	}
}
