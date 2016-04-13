package eu.marioproject.marvin.eventbus.impl;

import org.glassfish.jersey.media.sse.EventOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.marvin.eventbus.Consumer;

public class ConsumerImpl implements Consumer {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private EventOutput eventOutput;
	
	public ConsumerImpl(EventOutput eventOutput) {
		this.eventOutput = eventOutput;
	}
	
	@Override
	public EventOutput getEventOutput() {
		return eventOutput;
	}

}
