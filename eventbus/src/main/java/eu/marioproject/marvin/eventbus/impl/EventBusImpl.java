package eu.marioproject.marvin.eventbus.impl;

import java.io.IOException;
import java.util.Dictionary;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.marvin.eventbus.EventBus;


@Component(configurationFactory = true, immediate = true)
@Service(EventBus.class)
public class EventBusImpl implements EventBus{

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private ComponentContext componentContext;
	
	private Dictionary<String,Object> configuration;
	
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

        this.configuration = configuration;  
        
        log.info(getClass() + " activated.", this);
    }
    
    @Deactivate
    protected void deactivate(ComponentContext context) {
        log.info("in " + getClass() + " deactivate with context " + context);
    }

}
