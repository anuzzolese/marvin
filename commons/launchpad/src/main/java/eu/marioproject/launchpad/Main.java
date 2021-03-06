package eu.marioproject.launchpad;

/**
 * author: Andrea Nuzzolese
 */

import static org.apache.sling.launchpad.base.shared.SharedConstants.SLING_HOME;

import java.io.File;
import java.security.AllPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static final String DEFAULT_HOME = "mario-km";
    /**
     * If this argument is set ESWC is started without a securitymanager
     */
    public static final String NOSECURITYARG = "-no-security";
    /**
     * this
     */
    private static final String PRINTHELPARG = "-h";
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        
        String home = System.getProperties().getProperty(SLING_HOME);
        if(home == null){
            home = new File(DEFAULT_HOME).getAbsolutePath();
            System.setProperty(SLING_HOME, home);
        } //else do not override user configured values
        List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        if(argsList.contains(PRINTHELPARG)){
            doHelp();
            System.exit(0);
        }
        if (argsList.contains(NOSECURITYARG)) {
        	argsList.remove(NOSECURITYARG);
        } else {
        	args = argsList.toArray(new String[argsList.size()]);
	        Policy.setPolicy(new Policy() {
				@Override
				public PermissionCollection getPermissions(ProtectionDomain domain) {
					PermissionCollection result = new Permissions();
					result.add(new AllPermission());
					return result;
				}
			});
	        System.setSecurityManager(new SecurityManager());
        }
        //now use the standard Apache Sling launcher to do the job
        org.apache.sling.launchpad.app.Main.main(argsList.toArray(new String[argsList.size()]));
    }
    /** copied form {@link org.apache.sling.launchpad.app.Main} and extended for MARIO options */
    private static void doHelp() {
        System.out.println("usage: "
            + Main.class.getName()
            + " [ start | stop | status ] [ -j adr ] [ -l loglevel ] [ -f logfile ] [ -c slinghome ] [ -i launchpadhome ] [ -a address ] [ -p port ] [-no-security] { -Dn=v } [ -h ]");

        System.out.println("    start         listen for control connection (uses -j)");
        System.out.println("    stop          terminate running ESWC (uses -j)");
        System.out.println("    status        check whether ESWC is running (uses -j)");
        System.out.println("    -j adr        host and port to use for control connection in the format '[host:]port' (default 127.0.0.1:0)");
        System.out.println("    -l loglevel   the initial loglevel (0..4, FATAL, ERROR, WARN, INFO, DEBUG)");
        System.out.println("    -f logfile    the log file, \"-\" for stdout (default logs/error.log)");
        System.out.println("    -c slinghome  the Stamnol context directory (default stanbol)");
        System.out.println("    -i launchpadhome  the launchpad directory (default slinghome)");
        System.out.println("    -a address    the interfact to bind to (use 0.0.0.0 for any)");
        System.out.println("    -p port       the port to listen to (default 8080)");
        System.out.println("    -r path       the root servlet context path for the http service (default is /)");
        System.out.println("    -no-security  runs Stanbol without a security manager");
        System.out.println("    -Dn=v        sets java system property 'n' to value 'v'");
        System.out.println("    -h            prints this usage message");
    }
}
