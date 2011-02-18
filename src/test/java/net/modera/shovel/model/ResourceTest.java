package net.modera.shovel.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResourceTest {
	
	@Test
	public void testConnect() {
		Resource boo = new Resource();
		Resource foo = new Resource();
		boo.connect(foo);
		
		assertEquals(1, boo.getConnections().size());
		assertSame(foo, boo.getConnections().get(0).getTargetResource());
	}
}
