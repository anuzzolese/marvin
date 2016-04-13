package eu.marioproject.marvin.eventbus;

import java.util.Map;

public interface HTTPPublisher {

	Map<String, Object> getConfiguration();
	
	Object getQueue(String queueId);
	
	boolean hasQueue(String queueId);
	
	void registerConsumer(Consumer consumer);
	
	void publish(String content);
	
}
