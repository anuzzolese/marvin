package eu.marioproject.marvin.eventbus;

import java.util.Collection;

public interface ConsumerRegister {

	void register(String topic);
	
	void unregister(Consumer consumer);
	
	void unregister(String topic);
	
	Collection<Consumer> getConsumers(String topic);
	
}
