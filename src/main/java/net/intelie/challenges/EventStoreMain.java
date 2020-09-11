package net.intelie.challenges;

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
		ConcurrentNavigableMap<Long, Event> queryMap = eventMemory.subMap(startTime, true, endTime, false);
		EventIterator iterator = new EventIteratorMain(queryMap.entrySet());
		while(iterator.moveNext()) {
			Event currentEvent = iterator.current();
			if(currentEvent.type().equals(type)) {
				queryEvents.put(currentEvent.timestamp(), currentEvent);
			}
		}
		try {
			iterator.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		iterator = new EventIteratorMain(queryEvents.entrySet());
		return iterator;
	}

}
