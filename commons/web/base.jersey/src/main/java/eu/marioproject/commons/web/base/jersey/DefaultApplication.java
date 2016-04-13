package eu.marioproject.commons.web.base.jersey;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Define the list of available resources and providers to be used by the JAX-RS Endpoint.
 */
public class DefaultApplication extends Application {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(DefaultApplication.class);

    protected final Set<Class<?>> contributedClasses = new HashSet<Class<?>>();

    protected final Set<Object> contributedSingletons = new HashSet<Object>();


    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.addAll(contributedClasses);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();
        singletons.addAll(contributedSingletons);
        return singletons;
    }

    public void contributeClasses(Set<Class<?>> classes) {
        contributedClasses.addAll(classes);
    }

    public void contributeSingletons(Set<Object> singletons) {
        contributedSingletons.addAll(singletons);
    }

}
