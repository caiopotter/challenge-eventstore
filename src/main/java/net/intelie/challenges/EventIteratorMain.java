package net.intelie.challenges;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class EventIteratorMain implements EventIterator {
	private Iterator<Entry<Long, Event>> iterator;
	private Event currentEvent = null;
	
	public EventIteratorMain(Set<Entry<Long, Event>> events) {
		this.iterator = events.iterator();
	}
	
	@Override
	public void close() throws Exception {
		System.out.println("Closing resource");
	}

	@Override
	public boolean moveNext() {
		if(iterator.hasNext()) {
			currentEvent =  iterator.next().getValue();
			return true;
		}
		return false;
	}

	@Override
	public Event current() {
		if(currentEvent != null) {
			return currentEvent;
		}
		return null;
	}

	@Override
	public void remove() {
		iterator.remove();

	}

}
