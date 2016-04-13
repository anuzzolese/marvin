package eu.marioproject.commons.web.base;

import java.util.List;
import java.util.Set;


/**
 * Interface to be implemented by bundles that want to customize the stanbol web interface and REST API by
 * contributing static resources, JAX-RS resources and Freemarker views.
 * 
 * @deprecated The whiteboard pattern should be used for registering OSGi components and navigations links, Links and ScriptResource should be in the template
 * TODO: add some kind of ordering information
 */
@Deprecated
public interface WebFragment {

    /**
     * Name of the fragment. Should be a lowercase short name without any kind of special character, so as to
     * be used as a path component in the URL of the static resources.
     */
    String getName();

    /**
     * Set of JAX-RS resources provided as classes.
     * <p>
     * Note: those classes should be visible: use the Export-Package bundle declaration to export their
     * packages.
     */
    Set<Class<?>> getJaxrsResourceClasses();

    /**
     * Set of JAX-RS resources provided as singleton instances.
     * <p>
     * Note: those objects should be visible: use the Export-Package bundle declaration to export their
     * packages.
     */
    Set<Object> getJaxrsResourceSingletons();


    /**
     * CSS and favicon resources to be linked in the head of all HTML pages controlled by the NavigationMixin
     * abstract resource. The resources will be published under:
     * 
     * ${it.staticRootUrl}/${link.fragmentName}/${link.relativePath}
     */
    List<LinkResource> getLinkResources();

    /**
     * Javascript resources to be linked in the head of all HTML pages controlled by the NavigationMixin
     * abstract resource. The resources will be published under:
     * 
     * ${it.staticRootUrl}/${script.fragmentName}/${script.relativePath}
     */
    List<ScriptResource> getScriptResources();

    /**
     * List of link descriptions to contribute to the main navigation menu.
     */
    List<NavigationLink> getNavigationLinks();

}
