package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
	private Event e;
	private Date d;
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		e = new Event("Placed BLACK stone at 4, 6");   // (1)
		d = Calendar.getInstance().getTime();   // (2)
	}
	
	@Test
	public void testEvent() {
		assertEquals("Placed BLACK stone at 4, 6", e.getDescription());
		assertTrue(d.getTime() - e.getDate().getTime() < 1000);
	}

	@Test
	public void testToString() {
		assertEquals(d.toString() + "\n" + "Placed BLACK stone at 4, 6", e.toString());
	}

    @Test
    public void testEquals() {
        assertFalse(e.equals(null));
        assertFalse(e.equals("test"));
        assertTrue(e.equals(new Event("Placed BLACK stone at 4, 6")));
    }

    @Test
    public void testHashCode() {
        e = new Event("test event");
        Map<Event, String> eventList = new HashMap<>();
        eventList.put(e, "test");
        assertEquals("test", eventList.get(new Event("test event")));
    }
}
