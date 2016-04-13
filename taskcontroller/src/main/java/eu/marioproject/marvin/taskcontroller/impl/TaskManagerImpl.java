package eu.marioproject.marvin.taskcontroller.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.marvin.reasoner.api.ReasoningManager;
import eu.marioproject.marvin.taskcontroller.api.Status;
import eu.marioproject.marvin.taskcontroller.api.Task;
import eu.marioproject.marvin.taskcontroller.api.TaskManager;

/**
 * Basic implementation of the task manager.
 * 
 * @author Andrea Nuzzolese
 *
 */
@Component(immediate = true)
@Service(TaskManager.class)
public class TaskManagerImpl implements TaskManager, ServiceListener {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private Map<Integer, Task> taskMap;
	
	private ComponentContext context;
	
	@Reference
	private ReasoningManager reasoningManager;

	@Override
	public Status startTask(int taskID) {
		Task task = taskMap.get(taskID);
		return startTask(task);
	}

	@Override
	public Status startTask(Task task) {
		return task.start();
	}

	@Override
	public Status stopTask(int taskID) {
		Task task = taskMap.get(taskID);
		return stopTask(task);
	}

	@Override
	public Status stopTask(Task task) {
		return task.stop();
	}
	
	@Override
	public Status getStatus(int taskID){
		Task task = taskMap.get(taskID);
		return getStatus(task);
	}
	
	@Override
	public Status getStatus(Task task){
		return task.getStatus();
	}
	
	@Override
	public Collection<Task> getTasks(){
		return taskMap.values();
	}
	
	@Override
	public Collection<Task> getTasks(Status status){
		Collection<Task> tasks = new ArrayList<>();
		for(Task task : taskMap.values()){
			if(task.getStatus().equals(status)) tasks.add(task);
		}
		return tasks;
	}

	@Override
	public void performReasoning() {
		reasoningManager.performReasoning();
	}
	
	@Activate
	protected void activate(final ComponentContext context) {
		taskMap = new HashMap<Integer,Task>();
		this.context = context;
		context.getBundleContext().addServiceListener(this);
        activate((Dictionary<String,Object>) context.getProperties());
    }

    /**
     * Should be called within both OSGi and non-OSGi environments.
     * 
     * @param configuration
     * @throws IOException
     */
    protected void activate(Dictionary<String,Object> configuration) {

        log.info(getClass() + " activated.");
    }

	@Deactivate
	protected void deactivate(final ComponentContext context) {
		context.getBundleContext().removeServiceListener(this);
		taskMap = null;
		this.context = null;
		log.info("in " + getClass() + " deactivate with context " + context);
	}
	
	@Override
    public void serviceChanged(ServiceEvent event) {
        
		ServiceReference<?> serviceReference = event.getServiceReference();

        Object service = context.getBundleContext().getService(serviceReference);

        if(service != null){
	        log.info("A changed occurred to the service " + service.toString());
	        if (service instanceof Task) {
	            Task task = (Task) context.getBundleContext().getService(serviceReference);
	
	            switch (event.getType()) {
	                case ServiceEvent.MODIFIED:
	                    updateTask(task);
	                    log.info("Modified task " + task.getClass());
	                    break;
	                case ServiceEvent.REGISTERED:
	                	registerTask(task);
	                    log.info("Registered task " + task.getClass());
	                    break;
	                case ServiceEvent.UNREGISTERING:
	                    unregisterTask(task);
	                    log.info("Unregistered task " + task.getClass());
	                    break;
	
	                default:
	                    break;
	            }
	
	            log.info("The " + getClass() + " manages " + taskMap.entrySet().size() + " different tasks.");
	        }
        }
        
    }

	@Override
	public void registerTask(Task task) {
		taskMap.put(task.getID(), task);
	}

	@Override
	public void unregisterTask(Task task) {
		taskMap.remove(task.getID());
		
	}

	@Override
	public void updateTask(Task task) {
		unregisterTask(task);
		registerTask(task);
	}

	@Override
	public Task getTask(int taskID) {
		return taskMap.get(taskID);
	}

}
