package eu.marioproject.commons.web.base.jersey;

import java.util.List;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import eu.marioproject.commons.web.base.LinkResource;
import eu.marioproject.commons.web.base.NavigationLink;
import eu.marioproject.commons.web.base.ScriptResource;
import eu.marioproject.commons.web.base.resource.LayoutConfiguration;

/**
 * This class is here for supporting legacy templates
 *
 * @deprecated this sets global css-links and script imports, they should be set
 * specifically in templates
 */
@Component
@Service({LayoutConfiguration.class, EditableLayoutConfiguration.class})
public class EditableLayoutConfiguration implements LayoutConfiguration {

    private List<NavigationLink> navigationLinks;
    private String rootUrl;
    
    public static final String SYSTEM_CONSOLE = "system/console";
    private String staticResourcesRootUrl;
    private List<LinkResource> linkResources;
    private List<ScriptResource> scriptResources;

    
    @Override
    public List<NavigationLink> getNavigationLinks() {
        return navigationLinks;
    }
    
    
    @Override
    public String getRootUrl() {
        return rootUrl;
    }
    
    void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    @Override
    public String getStaticResourcesRootUrl() {
        return staticResourcesRootUrl;
    }

    @Override
    public List<LinkResource> getRegisteredLinkResources() {
        return linkResources;
    }

    @Override
    public List<ScriptResource> getRegisteredScriptResources() {
        return scriptResources;
    }

    void setStaticResourcesRootUrl(String staticResourcesRootUrl) {
        this.staticResourcesRootUrl = staticResourcesRootUrl;
    }

    void setLinkResources(List<LinkResource> linkResources) {
        this.linkResources = linkResources;
    }

    void setScriptResources(List<ScriptResource> scriptResources) {
        this.scriptResources = scriptResources;
    }

    void setNavigationsLinks(List<NavigationLink> navigationLinks) {
        this.navigationLinks = navigationLinks;
    }
}
