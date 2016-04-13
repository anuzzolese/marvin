package eu.marioproject.commons.installer.provider.bundle;

import org.apache.sling.installer.api.InstallableResource;

/**
 * @author Andrea Nuzzolese
 */
public class BundleInstallerConstants {

    /**
     * The name of the header field used for the 
     * <a href="http://www.aqute.biz/Snippets/Extender"> The OSGi extender 
     * pattern </a>.
     */
    public static final String BUNDLE_INSTALLER_HEADER = "Install-Path";

    /**
     * The schema used for {@link InstallableResource}s created by the
     * bundle provider.
     */
    public static final String PROVIDER_SCHEME = "bundleinstall";

}
