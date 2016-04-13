package eu.marioproject.marvin.web.eventbus.helloworld;

public class HelloWorld {
	
	
	public static final String EVENTBUS_REMOTE_LOCATION = "http://localhost:8080/eventbus";
	public static final String TOPIC = "HELLO";

	
	
	public static void main(String[] args) {
		HelloWorldPublisher publisher = new HelloWorldPublisher();
		publisher.register();
		
		Thread[] threads = new Thread[5];
		
		for(int i=0; i<threads.length; i++){
			threads[i] = new Thread(new HelloWorldConsumer(String.valueOf(i)));
			threads[i].start();
		}
		
		publisher.publish();
		
		for(int i=0; i<threads.length; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
