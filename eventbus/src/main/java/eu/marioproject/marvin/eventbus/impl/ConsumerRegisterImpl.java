package eu.marioproject.marvin.eventbus.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.marvin.eventbus.Consumer;
import eu.marioproject.marvin.eventbus.ConsumerRegister;
import eu.marioproject.marvin.eventbus.HTTPPublisher;
import eu.marioproject.marvin.eventbus.HTTPPublisherConfiguration;
import eu.marioproject.marvin.eventbus.HTTPPublisherRegister;

@Component(immediate = true)
@Service(ConsumerRegister.class)
public class ConsumerRegisterImpl implements ConsumerRegister {

	
private final Logger log = LoggerFactory.getLogger(getClass());
	
	private ComponentContext context;
	
	@Activate
	protected void activate(final ComponentContext context) {
		this.context = context;
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
		this.context = null;
		log.info("in " + getClass() + " deactivate with context " + context);
	}

	@Override
	public void register(String topic) {
		BundleContext bundleContext = context.getBundleContext();
		ServiceReference<ConfigurationAdmin> reference = bundleContext.getServiceReference(ConfigurationAdmin.class);
        ConfigurationAdmin configAdmin = bundleContext.getService(reference);
        
        try {
			Configuration config = configAdmin.createFactoryConfiguration(ConsumerImpl.class.getName(), null);
			
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(HTTPPublisherConfiguration.TOPIC, topic);
			
			config.update(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void unregister(Consumer consumer) {
		
		
	}

	@Override
	public void unregister(String topic) {

		BundleContext bundleContext = context.getBundleContext();
		ServiceReference<ConfigurationAdmin> reference = bundleContext.getServiceReference(ConfigurationAdmin.class);
        
        ConfigurationAdmin configAdmin = (ConfigurationAdmin) bundleContext.getService(reference);
        
        try {
        	//Configuration config = configAdmin.getConfiguration(serviceId);
        	Configuration[] configurations = configAdmin.listConfigurations("(" + HTTPPublisherConfiguration.TOPIC + "=" + topic + ")");
        	for(Configuration config : configurations)
        		config.delete();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    
		
		
	}

	@Override
	public Collection<Consumer> getConsumers(String topic) {
		Collection<Consumer> consumers = new ArrayList<Consumer>();
		BundleContext bundleContext = context.getBundleContext();
		
		Collection<ServiceReference<Consumer>> serviceReferences = null;
		try {
			serviceReferences = bundleContext.getServiceReferences(Consumer.class, "(" + HTTPPublisherConfiguration.TOPIC + "=" + topic + ")");
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(serviceReferences != null){
			Iterator<ServiceReference<Consumer>> serviceReferencesIterator = serviceReferences.iterator();
			
			while(serviceReferencesIterator.hasNext()){
				ServiceReference<Consumer> serviceReference = serviceReferencesIterator.next();
				Consumer consumer = bundleContext.getService(serviceReference);
				consumers.add(consumer);
			}
		}
		return consumers;
	}
}
