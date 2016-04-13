package eu.marioproject.marvin.taskcontroller.api;

import java.util.Collection;

public interface TaskManager {

	Status startTask(int taskID);
	Status startTask(Task task);
	
	Status stopTask(int taskID);
	Status stopTask(Task task);
	
	Status getStatus(int taskID);
	Status getStatus(Task task);
	
	Collection<Task> getTasks();
	Collection<Task> getTasks(Status status);
	
	Task getTask(int taskID);
	
	void registerTask(Task task);
	void unregisterTask(Task task);
	void updateTask(Task task);
	
	void performReasoning();
}
