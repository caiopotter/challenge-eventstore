package net.intelie.challenges;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class EventStoreMain implements EventStore {
	ConcurrentSkipListMap<Long, Event> eventMemory = new ConcurrentSkipListMap<Long, Event>();
	EventIterator iterator = new EventIteratorMain(eventMemory.entrySet());
	
	@Override
	public void insert(Event event) {
		eventMemory.put(event.timestamp(), event);
		iterator.updateIterator(eventMemory.entrySet());
	}

	@Override
	public void removeAll(String type) {
		Iterator<Entry<Long, Event>> removeIterator = eventMemory.entrySet().iterator();
		while(removeIterator.hasNext()) {
			Entry<Long, Event> currentEvent = removeIterator.next();
			if(currentEvent.getValue().type().equals(type)) {
				eventMemory.remove(currentEvent.getKey());
			}
		}
		iterator.updateIterator(eventMemory.entrySet());
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		ConcurrentNavigableMap<Long, Event> query = eventMemory.subMap(startTime, true, endTime, false);
		Iterator<Entry<Long, Event>> queryIt = query.entrySet().iterator();
		while(queryIt.hasNext()) {
			Entry<Long, Event> currentEvent = queryIt.next();
			if(currentEvent.getValue().type().equals(type)) {
				query.remove(currentEvent.getKey());
			}
		}
		iterator.updateIterator(query.entrySet());
		return iterator;
	}
	
	//ERASE METHOD!
	@Override
	public void printAll() {
		Event selectedEvent = iterator.current();
		if(selectedEvent != null) {
			System.out.println(selectedEvent.timestamp() + " - " + selectedEvent.type());
		}else {
			System.out.println("Fim");
		}
		
	}

}
