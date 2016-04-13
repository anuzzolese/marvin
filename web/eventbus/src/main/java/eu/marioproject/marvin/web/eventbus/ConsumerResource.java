package eu.marioproject.marvin.web.eventbus;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ManagedAsync;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.commons.web.base.resource.BaseMarvinResource;
import eu.marioproject.marvin.eventbus.HTTPPublisher;
import eu.marioproject.marvin.eventbus.HTTPPublisherRegister;
import eu.marioproject.marvin.eventbus.impl.ConsumerImpl;

@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Path("/eventbus/{topic}/subscribe")
public class ConsumerResource extends BaseMarvinResource {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	
	private ComponentContext componentContext;
	
	@Reference
	private HTTPPublisherRegister register;
	
	@GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
	@ManagedAsync
    public EventOutput listenToBroadcast(@PathParam("topic") String topic) {
		
		final EventOutput eventOutput = new EventOutput();
        
        HTTPPublisher publisher = register.getPublisher(topic);
        publisher.registerConsumer(new ConsumerImpl(eventOutput));
        
        log.info("Registered client on topic " + topic);
        
        return eventOutput;
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
        
        activate((Dictionary<String,Object>) context.getProperties());
    }
	
	/**
     * Should be called within both OSGi and non-OSGi environments.
     * 
     * @param configuration
     * @throws IOException
     */
    protected void activate(Dictionary<String,Object> configuration) {

        log.info(getClass() + " activated.");
    }

	@Deactivate
	protected void deactivate(final ComponentContext context) {
		
		log.info("in " + getClass() + " deactivate with context " + context);
	}
}
