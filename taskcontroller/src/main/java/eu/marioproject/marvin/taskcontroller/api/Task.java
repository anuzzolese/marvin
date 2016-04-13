package eu.marioproject.marvin.taskcontroller.api;

public interface Task {

	public static final String TASK_ID = "eu.marioproject.marvin.taskcontroller.api.id";
	public static final String TASK_DESCRIPTION = "eu.marioproject.marvin.taskcontroller.api.description";
	
	int getID();
	
	String getDescription();
	
	Status getStatus();
	
	Status start();
	Status stop();
	
}
