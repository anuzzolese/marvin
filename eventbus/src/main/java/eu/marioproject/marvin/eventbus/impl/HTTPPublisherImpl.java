package eu.marioproject.marvin.eventbus.impl;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.marvin.eventbus.Consumer;
import eu.marioproject.marvin.eventbus.HTTPPublisher;

@Component(configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, metatype = true)
@Service(HTTPPublisher.class)
public class HTTPPublisherImpl implements HTTPPublisher {
	
	
	public static final String PUBLISHER_ID = "eu.marioproject.marvin.eventbus.pulbisher.id";  
	
	@Property(name=HTTPPublisherImpl.PUBLISHER_ID, value="")
	private String id;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private ComponentContext componentContext;
	private SseBroadcaster broadcaster;
	
	private Map<String, Object> configuration;
	
	public HTTPPublisherImpl(){
		
	}
	
	public HTTPPublisherImpl(Map<String, Object> configuration) {
		this.configuration = configuration;
	}

	@Override
	public Map<String, Object> getConfiguration() {
		return configuration;
	}
	
	/**
     * Used to configure an instance within an OSGi container.
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Activate
    protected void activate(ComponentContext context) throws IOException {
        this.componentContext = context;
        log.info("in " + getClass() + " activate with context " + context);
        if (context == null) {
            throw new IllegalStateException("No valid" + ComponentContext.class + " parsed in activate!");
        }
        this.broadcaster = new SseBroadcaster();
        activate((Dictionary<String,Object>) context.getProperties());
    }

    /**
     * Should be called within both OSGi and non-OSGi environments.
     * 
     * @param configuration
     * @throws IOException
     */
    protected void activate(Dictionary<String,Object> configuration) {

        this.configuration = new HashMap<String,Object>();
        Enumeration<String> keys = configuration.keys();
        while(keys.hasMoreElements()){
        	String key = keys.nextElement();
        	this.configuration.put(key, configuration.get(key));
        }
        
        log.info(getClass() + " activated.", this);
    }
    
    @Deactivate
    protected void deactivate(ComponentContext context) {
    	this.configuration = null;
    	this.componentContext = null;
        log.info("in " + getClass() + " deactivate with context " + context);
    }

	@Override
	public Object getQueue(String queueId) {
		return configuration.get(queueId);
	}
	
	@Override
	public boolean hasQueue(String queueId) {
		return configuration.get(queueId) == null ? false : true;
	}
	
	@Override
	public void publish(String content){
		
		OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
        OutboundEvent outboundEvent = eventBuilder.name("message")
            .mediaType(MediaType.TEXT_PLAIN_TYPE)
            .data(String.class, content)
            .build();
 
        broadcaster.broadcast(outboundEvent);
		
        /*
		BundleContext bundleContext = componentContext.getBundleContext();
			
		ServiceReference<EventAdmin> eventAdminReference = bundleContext.getServiceReference(EventAdmin.class);
	        if (eventAdminReference != null) {
	            EventAdmin eventAdmin = (EventAdmin) bundleContext.getService(eventAdminReference);
	
	            String eventId = HTTPPublisherConfiguration.PUBLISHER_ARTIFACT + "." +  id + "." + queueId;  
	
	            Event reportGeneratedEvent = new Event(eventId, content);
	
	            eventAdmin.postEvent(reportGeneratedEvent);
	        }
		}
		*/
    }
	
	@Override
	public void registerConsumer(Consumer consumer) {
		broadcaster.add(consumer.getEventOutput());
	}

}
