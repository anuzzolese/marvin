package eu.marioproject.marvin.eventbus;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;

public interface Consumer {
	
	EventOutput getEventOutput();
	
}
