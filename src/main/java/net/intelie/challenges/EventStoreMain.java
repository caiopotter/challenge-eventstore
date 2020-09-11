package net.intelie.challenges;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class EventStoreMain implements EventStore {
	ConcurrentSkipListMap<Long, Event> eventMemory = new ConcurrentSkipListMap<Long, Event>();
	
	@Override
	public void insert(Event event) {
		eventMemory.put(event.timestamp(), event);
	}

	@Override
	public void removeAll(String type) {
		EventIterator iterator = new EventIteratorMain(eventMemory.entrySet());
		while(iterator.moveNext()) {
			Event currentEvent = iterator.current();
			if(currentEvent.type().equals(type)) {
				System.out.println("Removido: " + currentEvent.type() + " - " + currentEvent.timestamp());
				iterator.remove();
			}
		}
		try {
			iterator.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		ConcurrentSkipListMap<Long, Event> queryEvents = new ConcurrentSkipListMap<Long, Event>();
		ConcurrentNavigableMap<Long, Event> query = eventMemory.subMap(startTime, true, endTime, false);
		Iterator<Entry<Long, Event>> queryIt = query.entrySet().iterator();
		while(queryIt.hasNext()) {
			Entry<Long, Event> currentEvent = queryIt.next();
			if(currentEvent.getValue().type().equals(type)) {
				queryEvents.put(currentEvent.getKey(), currentEvent.getValue());
			}
		}
		EventIterator iterator = new EventIteratorMain(queryEvents.entrySet());
		return iterator;
	}

}
