package eu.marioproject.commons.web.base.resource;

import java.util.List;

import eu.marioproject.commons.web.base.LinkResource;
import eu.marioproject.commons.web.base.NavigationLink;
import eu.marioproject.commons.web.base.ScriptResource;

/**
 * That's the data the Web Binding Provider need to provide
 */
public interface LayoutConfiguration {

    List<NavigationLink> getNavigationLinks();

    String getRootUrl();

    public String getStaticResourcesRootUrl();

    public List<LinkResource> getRegisteredLinkResources();

    public List<ScriptResource> getRegisteredScriptResources();
    
}
