package net.modera.shovel.graph;


import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import net.modera.shovel.traveler.ResourceTraveler;

import static org.junit.Assert.*;

public class ResourceTravelerProjectionTest {

	ResourceGraphProjection projection;
	
	@Before
	public void setUp() throws Exception {
		projection = new ResourceGraphProjection(mock(Shell.class));
	}

	@After
	public void tearDown() throws Exception {
		projection = null;
	}
	
	@Test
	public void projectionShouldAcceptTraveler()
	{
		ResourceTraveler traveler = mock(ResourceTraveler.class);
		projection.setTraveler(traveler);
		assertSame(traveler, projection.getTraveler());
	}

}
