package eu.marioproject.marvin.eventbus.impl;

import java.io.IOException;
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

import eu.marioproject.marvin.eventbus.HTTPPublisher;
import eu.marioproject.marvin.eventbus.HTTPPublisherConfiguration;
import eu.marioproject.marvin.eventbus.HTTPPublisherRegister;

@Component(immediate = true)
@Service(HTTPPublisherRegister.class)
public class HTTPPublisherRegisterImpl implements HTTPPublisherRegister {

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
	public void register(HTTPPublisherConfiguration httpPublisher) {
		BundleContext bundleContext = context.getBundleContext();
		ServiceReference<ConfigurationAdmin> reference = bundleContext.getServiceReference(ConfigurationAdmin.class);
        ConfigurationAdmin configAdmin = bundleContext.getService(reference);
        
        try {
			Configuration config = configAdmin.createFactoryConfiguration(HTTPPublisherImpl.class.getName(), null);
			
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(HTTPPublisherImpl.PUBLISHER_ID, httpPublisher.getId());
			Map<String, Object> configurationProps = httpPublisher.getConfiguration();
			Set<String> keys = configurationProps.keySet();
			for(String key : keys){
				props.put(key, configurationProps.get(key));
			}
			
			config.update(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	@Override
	public HTTPPublisher getPublisher(String publisherId) {
		
		HTTPPublisher httpPublisher = null;
		
		BundleContext bundleContext = context.getBundleContext();
		
		Collection<ServiceReference<HTTPPublisher>> serviceReferences = null;
		try {
			serviceReferences = bundleContext.getServiceReferences(HTTPPublisher.class, "(" + HTTPPublisherImpl.PUBLISHER_ID + "=" + publisherId + ")");
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(serviceReferences != null){
			Iterator<ServiceReference<HTTPPublisher>> serviceReferencesIterator = serviceReferences.iterator();
			
			while(serviceReferencesIterator.hasNext() && httpPublisher == null){
				ServiceReference<HTTPPublisher> serviceReference = serviceReferencesIterator.next();
				httpPublisher = bundleContext.getService(serviceReference);
			}
		}
		return httpPublisher;
    }
	
	@Override
    public void unregister(String publisherId) {
		BundleContext bundleContext = context.getBundleContext();
		ServiceReference<ConfigurationAdmin> reference = bundleContext.getServiceReference(ConfigurationAdmin.class);
        
        ConfigurationAdmin configAdmin = (ConfigurationAdmin) bundleContext.getService(reference);
        
        try {
        	//Configuration config = configAdmin.getConfiguration(serviceId);
        	Configuration[] configurations = configAdmin.listConfigurations("(" + HTTPPublisherImpl.PUBLISHER_ID + "=" + publisherId + ")");
        	for(Configuration config : configurations)
        		config.delete();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
	
	
}
