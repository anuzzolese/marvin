package eu.marioproject.commons.web.base.jersey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.commons.web.base.CorsHelper;
import eu.marioproject.commons.web.base.LinkResource;
import eu.marioproject.commons.web.base.NavigationLink;
import eu.marioproject.commons.web.base.ScriptResource;
import eu.marioproject.commons.web.base.WebFragment;

/**
 * Jersey-based RESTful endpoint for the Marvin framework.
 * <p>
 * This OSGi component serves as a bridge between the OSGi context and the Servlet context available to JAX-RS
 * resources.
 */
@Component(immediate = true, metatype = true)
@References({
    @Reference(name = "webFragment", 
        referenceInterface = WebFragment.class, 
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, 
        policy = ReferencePolicy.DYNAMIC),
    @Reference(name="component", referenceInterface=Object.class, 
        target="(javax.ws.rs=true)", 
		cardinality=ReferenceCardinality.OPTIONAL_MULTIPLE, 
        policy=ReferencePolicy.DYNAMIC),
    @Reference(name="navigationLink", referenceInterface=NavigationLink.class, 
		cardinality=ReferenceCardinality.OPTIONAL_MULTIPLE, 
        policy=ReferencePolicy.DYNAMIC)})
public class JerseyEndpoint {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Property(value = "/")
    public static final String ALIAS_PROPERTY = "eu.marioproject.commons.web.alias";

    @Property(value = "/static")
    public static final String STATIC_RESOURCES_URL_ROOT_PROPERTY = "eu.marioproject.commons.web.static.url";
    
    
    @Reference
    private EditableLayoutConfiguration layoutConfiguration;

    /**
     * The origins allowed for multi-host requests
     */
    @Property(cardinality = 100, value = {"*"})
    public static final String CORS_ORIGIN = "eu.marioproject.commons.web.cors.origin";

    @Property(cardinality = 100, value = {"Location"})
    public static final String CORS_ACCESS_CONTROL_EXPOSE_HEADERS = "eu.marioproject.commons.web.cors.access_control_expose_headers";

    @Reference
    HttpService httpService;

    protected ComponentContext componentContext;

    protected ServletContext servletContext;

    protected final List<WebFragment> webFragments = new ArrayList<WebFragment>();

    protected final List<String> registeredAliases = new ArrayList<String>();

    protected Set<String> corsOrigins;

    protected Set<String> exposedHeaders;
    private Set<Object> components = new HashSet<Object>();
    private List<NavigationLink> navigationLinks = new ArrayList<NavigationLink>();

    public Dictionary<String,String> getInitParams() {
        Dictionary<String,String> initParams = new Hashtable<String,String>();
        // make jersey automatically turn resources into Viewable models and
        // hence lookup matching freemarker templates
        initParams.put("com.sun.jersey.config.feature.ImplicitViewables", "true");
        return initParams;
    }

    @Activate
    protected void activate(ComponentContext ctx) throws IOException,
                                                 ServletException,
                                                 NamespaceException,
                                                 ConfigurationException {
        componentContext = ctx;
        // init corsOrigins
        Object values = componentContext.getProperties().get(CORS_ORIGIN);
        if (values instanceof String && !((String) values).isEmpty()) {
            corsOrigins = Collections.singleton((String) values);
        } else if (values instanceof String[]) {
            corsOrigins = new HashSet<String>(Arrays.asList((String[]) values));
        } else if (values instanceof Iterable<?>) {
            corsOrigins = new HashSet<String>();
            for (Object value : (Iterable<?>) values) {
                if (value != null && !value.toString().isEmpty()) {
                    corsOrigins.add(value.toString());
                }
            }
        } else {
            throw new ConfigurationException(CORS_ORIGIN,
                    "CORS origin(s) MUST be a String, String[], Iterable<String> (value:" + values + ")");
        }

        // parse headers to be exposed
        values = componentContext.getProperties().get(CORS_ACCESS_CONTROL_EXPOSE_HEADERS);
        if (values instanceof String && !((String) values).isEmpty()) {
            exposedHeaders = Collections.singleton((String) values);
        } else if (values instanceof String[]) {
            exposedHeaders = new HashSet<String>(Arrays.asList((String[]) values));
        } else if (values instanceof Iterable<?>) {
            exposedHeaders = new HashSet<String>();
            for (Object value : (Iterable<?>) values) {
                if (value != null && !value.toString().isEmpty()) {
                    exposedHeaders.add(value.toString());
                }
            }
        } else {
            exposedHeaders = new HashSet<String>();
        }
        if (!webFragments.isEmpty()) {
            initJersey();
        }
    }

    /** Initialize the Jersey subsystem */
    private synchronized void initJersey() throws NamespaceException, ServletException {
        if (componentContext == null) {
            //we have not yet been activated
            return;
        }
        //end of STANBOL-1073 work around
        if (componentContext == null) {
            log.debug(" ... can not init Jersey Endpoint - Component not yet activated!");
            //throw new IllegalStateException("Null ComponentContext, not activated?");
            return;
        }

        shutdownJersey();

        log.info("(Re)initializing the Stanbol Jersey subsystem");

        // register all the JAX-RS resources into a a JAX-RS application and bind it to a configurable URL
        // prefix
        DefaultApplication app = new DefaultApplication();
        String staticUrlRoot = (String) componentContext.getProperties().get(
            STATIC_RESOURCES_URL_ROOT_PROPERTY);
        
        log.info("STATIC RESOURCES URL ROOT PROPERTY: {}", staticUrlRoot);
        
        String applicationAlias = (String) componentContext.getProperties().get(ALIAS_PROPERTY);

        // incrementally contribute fragment resources
        List<LinkResource> linkResources = new ArrayList<LinkResource>();
        List<ScriptResource> scriptResources = new ArrayList<ScriptResource>();
        for (WebFragment fragment : webFragments) {
            log.debug("Registering web fragment '{}' into jaxrs application", fragment.getName());
            linkResources.addAll(fragment.getLinkResources());
            scriptResources.addAll(fragment.getScriptResources());
            navigationLinks.removeAll(fragment.getNavigationLinks());
            navigationLinks.addAll(fragment.getNavigationLinks());
            app.contributeClasses(fragment.getJaxrsResourceClasses());
            app.contributeSingletons(fragment.getJaxrsResourceSingletons());
        }
        app.contributeSingletons(components);
        Collections.sort(linkResources);
        Collections.sort(scriptResources);
        Collections.sort(navigationLinks);

        // bind the aggregate JAX-RS application to a dedicated servlet
        
        ServletContainer container = new ServletContainer(
                ResourceConfig.forApplication(app));
        
        Bundle appBundle = componentContext.getBundleContext().getBundle();
        httpService.registerServlet(applicationAlias, container, getInitParams(), null);
        registeredAliases.add(applicationAlias);

        // services
        servletContext = container.getServletContext();
        servletContext.setAttribute(BundleContext.class.getName(), componentContext.getBundleContext());
        layoutConfiguration.setRootUrl(applicationAlias);
        //servletContext.setAttribute(BaseStanbolResource.ROOT_URL, applicationAlias);
        layoutConfiguration.setStaticResourcesRootUrl(staticUrlRoot);
        //servletContext.setAttribute(BaseStanbolResource.STATIC_RESOURCES_ROOT_URL, staticUrlRoot);
        layoutConfiguration.setLinkResources(linkResources);
        //servletContext.setAttribute(BaseStanbolResource.LINK_RESOURCES, linkResources);
        layoutConfiguration.setScriptResources(scriptResources);
        //servletContext.setAttribute(BaseStanbolResource.SCRIPT_RESOURCES, scriptResources);
        layoutConfiguration.setNavigationsLinks(navigationLinks);
        //servletContext.setAttribute(BaseStanbolResource.NAVIGATION_LINKS, navigationLinks);
        servletContext.setAttribute(CorsHelper.ORIGIN, corsOrigins);
        servletContext.setAttribute(CorsHelper.EXPOSE_HEADERS, exposedHeaders);
        
        

        log.info("JerseyEndpoint servlet registered at {}", applicationAlias);
    }

    /** Shutdown Jersey, if there's anything to do */
    private synchronized void shutdownJersey() {
        log.debug("Unregistering aliases {}", registeredAliases);
        for (String alias : registeredAliases) {
            httpService.unregister(alias);
        }
        registeredAliases.clear();
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
        shutdownJersey();
        servletContext = null;
        componentContext = null;
    }

    protected void bindWebFragment(WebFragment webFragment) throws IOException,
                                                           ServletException,
                                                           NamespaceException {
        // TODO: support some ordering for jax-rs resource and template overrides?
        webFragments.add(webFragment);
        initJersey();
    }

    protected void unbindWebFragment(WebFragment webFragment) throws IOException,
                                                             ServletException,
                                                             NamespaceException {
        navigationLinks.removeAll(webFragment.getNavigationLinks());
        webFragments.remove(webFragment);
        initJersey();
    }
    
    protected void bindComponent(Object component) throws IOException,
                                                          ServletException,
                                                          NamespaceException  {
        components.add(component);
        initJersey();
    }

    protected void unbindComponent(Object component) throws IOException,
                                                          ServletException,
                                                          NamespaceException  {
        components.remove(component);
        initJersey();
    }    
    
    protected void bindNavigationLink(NavigationLink navigationLink) {
        navigationLinks.add(navigationLink);
    }
    
    protected void unbindNavigationLink(NavigationLink navigationLink) {
        navigationLinks.remove(navigationLink);
    }
    
    public List<WebFragment> getWebFragments() {
        return webFragments;
    }
}
