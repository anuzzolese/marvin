package eu.marioproject.commons.web.base.resource;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;

/**
 * Mixin class to provide the controller method for the navigation template.
 * 
 * TODO: make the list of menu items dynamically contributed by WebFragments from the OSGi runtime.
 */
//not itself a component but subclasses must be
//according to http://felix.apache.org/documentation/subprojects/apache-felix-maven-scr-plugin.html
//"With the annotations the super class is required to have the Component annotation."
@Component(componentAbstract = true)
public class BaseMarvinResource extends TemplateLayoutConfiguration {
       
   
    @Reference
    private LayoutConfiguration layoutConfiguration;

    @Context
    protected UriInfo uriInfo;
    
    @Context
    protected ServletContext servletContext;

    protected LayoutConfiguration getLayoutConfiguration() {
        return layoutConfiguration;
    }
    
    protected UriInfo getUriInfo() {
        return uriInfo;
    }
    
    /**
     * Subclasses extend this object  to provide 
     * a data model for rendering with a Viewable Object
     */
    public abstract class ResultData extends TemplateLayoutConfiguration {

        @Override
        protected LayoutConfiguration getLayoutConfiguration() {
            return BaseMarvinResource.this.getLayoutConfiguration();
        }

        @Override
        protected UriInfo getUriInfo() {
            return BaseMarvinResource.this.getUriInfo();
        }


    }


}
