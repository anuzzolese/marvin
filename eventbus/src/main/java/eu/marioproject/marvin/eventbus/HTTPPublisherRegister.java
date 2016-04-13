package eu.marioproject.marvin.eventbus;

public interface HTTPPublisherRegister {

	void register(HTTPPublisherConfiguration httpPublisher);
	
	void unregister(String publisherId);
	
	HTTPPublisher getPublisher(String publisherId);
	
}
