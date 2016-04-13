package eu.marioproject.commons.installer.provider.bundle.impl;

import org.apache.sling.installer.api.OsgiInstaller;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author Andrea Nuzzolese
 *
 */
public class Activator implements BundleActivator, ServiceTrackerCustomizer {
    
    private static final Logger log = LoggerFactory.getLogger(Activator.class);

    private ServiceTracker installerTracker;
    
    private BundleContext bundleContext;
    
    private BundleInstaller bundleInstaller;
    
    @Override
    public void start(BundleContext context) throws Exception {
        bundleContext = context;
        //Note that this class implements ServiceTrackerCustomizer to init/stop
        // the BundleInstaller
        installerTracker = new ServiceTracker(context, OsgiInstaller.class.getName(),this);
        installerTracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        closeBundleInstaller();
        installerTracker.close();
        bundleContext = null;
    }
    
    @Override
    public Object addingService(ServiceReference reference) {
        Object service = bundleContext.getService(reference);
        if(service instanceof OsgiInstaller){
            initBundleInstaller((OsgiInstaller)service);
            return service;
        } else {
            return null;
        }
    }

    /* not needed for the OsgiInstaller */
    @Override
    public void modifiedService(ServiceReference arg0, Object arg1) { /* unused */ }

    @Override
    public void removedService(ServiceReference sr, Object s) {
        //stop the BundleInstaller
        closeBundleInstaller();
        //unget the service
        bundleContext.ungetService(sr);
    }
    
    private synchronized void initBundleInstaller(OsgiInstaller installer){
        if(bundleInstaller == null){
            log.info("start BundleInstaller");
            bundleInstaller = new BundleInstaller(installer, bundleContext);
        }
    }
    
    private synchronized void closeBundleInstaller(){
        if(bundleInstaller != null){
            log.info("close BundleInstaller");
            bundleInstaller.close();
            bundleInstaller = null;
        }
    }

}
