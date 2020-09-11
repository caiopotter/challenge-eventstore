package net.intelie.challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class EventStoreTest {
	static EventStore eventStore;
	
	@BeforeClass
	public static void insertingEvents() {
		eventStore = new EventStoreMain();
		eventStore.insert(new Event("some_type", 123L));
		eventStore.insert(new Event("some_type", 127L));
		eventStore.insert(new Event("some_type", 124L));
		eventStore.insert(new Event("another_type", 129L));
		eventStore.insert(new Event("another_type", 125L));
		eventStore.insert(new Event("some_type", 126L));
		eventStore.insert(new Event("some_type", 122L));
		eventStore.insert(new Event("another_type", 128L));
	}
	
    @Test
    public void addEventToEventMemoryTest() throws Exception {
    	EventStore eventStore = new EventStoreMain();
		eventStore.insert(new Event("some_type", 123L));
		EventIterator eventIterator = eventStore.query("some_type", 123L, 124L);
		
		Event currentEvent = null;
		while(eventIterator.moveNext()) {
			currentEvent = eventIterator.current();
		}
		
		assertNotNull(currentEvent);
		assertEquals(currentEvent.timestamp(), 123L);
    }
    
    @Test
    public void removeAllEventsOfOneTypeTest() {
    	eventStore.removeAll("some_type");
    	EventIterator eventIterator = eventStore.query("some_type", 1L, 999L);
    	
    	assertFalse(eventIterator.moveNext());
    	
    	eventIterator = eventStore.query("another_type", 1L, 999L);
    	
    	assertTrue(eventIterator.moveNext());
    }
    
    @Test
    public void getAllEventsInQueryTest() {
    	EventIterator eventIterator = eventStore.query("another_type", 1L, 999L);
    	
    	List<Long> eventsTimestamps = new ArrayList<Long>();
    	eventsTimestamps.add(125L);
    	eventsTimestamps.add(128L);
    	eventsTimestamps.add(129L);
    	int listIndex = 0;
    	
    	Event currentEvent = null;
		while(eventIterator.moveNext()) {
			currentEvent = eventIterator.current();
			assertEquals(eventsTimestamps.get(listIndex), (Long) currentEvent.timestamp());
			listIndex++;
		}
		assertNotNull(currentEvent);
    }
    
    @Test
    public void checkInitialQueryTimestampIsInclusiveTest() {
    	EventIterator eventIterator = eventStore.query("another_type", 125L, 126L);
    	
    	Event currentEvent = null;
		while(eventIterator.moveNext()) {
			currentEvent = eventIterator.current();
		}
		
		assertNotNull(currentEvent);
		assertEquals(currentEvent.timestamp(), 125L);
    }
    
    @Test
    public void checkFinalQueryTimestampIsExclusiveTest() {
		EventIterator eventIterator = eventStore.query("another_type", 123L, 125L);

    	assertFalse(eventIterator.moveNext());
    }
}