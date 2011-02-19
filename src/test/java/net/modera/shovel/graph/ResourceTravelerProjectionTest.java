package net.modera.shovel.graph;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import net.modera.shovel.traveler.ResourceTraveler;
import net.modera.shovel.model.*;

import static org.junit.Assert.*;

public class ResourceTravelerProjectionTest {

	ResourceTravelerProjection projection;
	
	@Before
	public void setUp() throws Exception {
		projection = new ResourceTravelerProjection();
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
