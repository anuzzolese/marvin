package eu.marioproject.marvin.tasks.playmusic;

import java.io.IOException;
import java.util.Dictionary;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.marvin.taskcontroller.api.Status;
import eu.marioproject.marvin.taskcontroller.api.Task;

/**
 * Implementation of a component for the task of playing music.
 * 
 * @author Andrea Nuzzolese
 *
 */
@Component(immediate = true, metatype = true)
@Service(Task.class)
public class PlayMusic implements Task {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final int id = 1;
	private final String defaultDescription = "Play music task.";
	
	private final String command = "/Applications/iTunes.app/Contents/MacOS/iTunes";
	
	@Property(name=Task.TASK_ID, intValue=id)
	int taskID;
	
	@Property(name=Task.TASK_DESCRIPTION, value=defaultDescription)
	String taskDescription;
	
	private Process process;
	
	private Status status;
	
	
	@Override
	public int getID() {
		return taskID;
	}

	@Override
	public Status start() {
		try {
			process = Runtime.getRuntime().exec(command);
			status = Status.running();
			log.info("Play music task is set to status {}", status);
			return status;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			status = Status.inactive();
			return Status.error();
		}
	}

	@Override
	public Status stop() {
		process.destroy();
		status = Status.inactive();
		return status;
	}

	@Override
	public String getDescription() {
		return taskDescription;
	}

	@Override
	public Status getStatus() {
		return status;
	}
	
	@Activate
	protected void activate(final ComponentContext context) {
		this.status = Status.inactive();
		activate((Dictionary<String,Object>) context.getProperties());
    }

    /**
     * Should be called within both OSGi and non-OSGi environments.
     * 
     * @param configuration
     * @throws IOException
     */
    protected void activate(Dictionary<String,Object> configuration) {

    	Integer id = (Integer)configuration.get(TASK_ID);
    	if(id != null) taskID = id;
    	
    	String description = (String)configuration.get(TASK_DESCRIPTION);
    	if(description != null && !description.isEmpty()) taskDescription = description;
    	
        log.info(getClass() + " activated.");
    }

	@Deactivate
	protected void deactivate(final ComponentContext context) {
		log.info("in " + getClass() + " deactivate with context " + context);
	}

}
