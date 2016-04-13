package eu.marioproject.marvin.web.eventbus;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.commons.web.base.resource.BaseMarvinResource;
import eu.marioproject.marvin.eventbus.HTTPPublisher;
import eu.marioproject.marvin.eventbus.HTTPPublisherConfiguration;
import eu.marioproject.marvin.eventbus.HTTPPublisherRegister;

/**
 * 
 * Web API for the Task Manager.
 * 
 * @author Andrea Nuzzolese
 *
 */

@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Path("/eventbus")
public class HTTPPublisherResource extends BaseMarvinResource {
       
   
	private Logger log = LoggerFactory.getLogger(getClass());
	
    @Reference
    private HTTPPublisherRegister httpPublisherRegister;
    
    @Context
    protected UriInfo uriInfo;
    
    @GET
    public Response test(){
    	
    	ResponseBuilder builder = null;
    	
    	builder = Response.ok();
    	
    	
    	
    	return builder.build();
    	
    }
    
    @PUT
    @Consumes("application/x-www-form-urlencoded")
    @Path("/{id}")
    public Response registerEventPublisher(@PathParam("id") String id){
    	
    	
    	HTTPPublisherConfiguration publisherConfiguration = new HTTPPublisherConfiguration(id, new HashMap<String,Object>());
    	
    	httpPublisherRegister.register(publisherConfiguration);
    	
    	ResponseBuilder builder = null;
    	
    	builder = Response.ok();
    	
    	
    	
    	return builder.build();
    	
    }
    
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/{topic}")
    public Response publishEvent(@PathParam("topic") String topic, @FormParam("body") String body){
    	
    	
    	HTTPPublisher publisher = httpPublisherRegister.getPublisher(topic);
    	
    	publisher.publish(body);
    	
    	ResponseBuilder builder = null;
    	
    	builder = Response.ok();
    	
    	return builder.build();
    	
    }
    
    @DELETE
    @Path("/{id}")
    public Response unregisterEventPublisher(@PathParam("id") String id, @Context HttpServletRequest request){
    	httpPublisherRegister.unregister(id);
    	
    	ResponseBuilder builder = null;
    	
    	builder = Response.ok();
    	
    	
    	
    	return builder.build();
    	
    }

    
    
    
    
}
