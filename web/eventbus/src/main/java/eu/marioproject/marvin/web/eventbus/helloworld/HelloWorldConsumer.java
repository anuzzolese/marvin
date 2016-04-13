package eu.marioproject.marvin.web.eventbus.helloworld;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

public class HelloWorldConsumer implements Runnable {

	private String id;
	
	public HelloWorldConsumer(String id) {
		this.id = id;
	}
	
	public void subscribe(){
		Client client = ClientBuilder.newBuilder()
		        .register(SseFeature.class).build();
		WebTarget target = client.target(HelloWorld.EVENTBUS_REMOTE_LOCATION + "/" + HelloWorld.TOPIC + "/subscribe");
		 
		EventInput eventInput = target.request().get(EventInput.class);
		while (!eventInput.isClosed()) {
		    final InboundEvent inboundEvent = eventInput.read();
		    if (inboundEvent == null) {
		        // connection has been closed
		        break;
		    }
		    System.out.println("Consumer " + id + " reads: " + inboundEvent.getName() + "; "
		        + inboundEvent.readData(String.class));
		}
	}
	
	@Override
	public void run() {
		subscribe();
	}

}
