package eu.marioproject.marvin.web.eventbus.helloworld;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class HelloWorldPublisher {

	public void register(){
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(HelloWorld.EVENTBUS_REMOTE_LOCATION + "/" + HelloWorld.TOPIC);
		
		Response response = target.request().put(Entity.entity("hello", MediaType.WILDCARD));
		
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}

		System.out.println("Publisher registered!");

		
	}
	
	public void publish(){
		Client client = ClientBuilder.newClient();

		MultivaluedMap<String,String> map = new MultivaluedMapImpl<String,String>();
		map.add("body", "Hello World!");
		WebTarget target = client.target(HelloWorld.EVENTBUS_REMOTE_LOCATION + "/" + HelloWorld.TOPIC);
		
		Response response = target.request().post(Entity.entity(map, MediaType.APPLICATION_FORM_URLENCODED));
		
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}
		
	}
	
}
