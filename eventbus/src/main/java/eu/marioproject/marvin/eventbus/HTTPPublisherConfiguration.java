package eu.marioproject.marvin.eventbus;

import java.util.Map;

public class HTTPPublisherConfiguration {

	public static final String PUBLISHER_ARTIFACT = "eu.marioproject.marvin.eventbus";
	public static final String EVENT_CONTENT = "eu.marioproject.marvin.eventbus.event.content";
	public static final String TOPIC = "eu.marioproject.marvin.eventbus.event.topic";
	
	private String id;
	private Map<String, Object> configuration;
	
	public HTTPPublisherConfiguration(String id, Map<String, Object> configuration) {
		this.id = id;
		this.configuration = configuration;
	}
	
	public Map<String, Object> getConfiguration() {
		return configuration;
	}
	
	public String getId() {
		return id;
	}
	
}
