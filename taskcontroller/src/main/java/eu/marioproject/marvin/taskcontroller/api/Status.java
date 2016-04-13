package eu.marioproject.marvin.taskcontroller.api;

public class Status {

	public static final int RUNNING = 1;
	public static final int INACTIVE = 2;
	public static final int ERROR = 0;
	
	private int code;
	
	private Status(int code){
		this.code = code;
	}
	
	public static Status running(){
		return new Status(RUNNING);
	}
	
	public static Status inactive(){
		return new Status(INACTIVE);
	}
	
	public static Status error(){
		return new Status(ERROR);
	}
	
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		switch (code) {
		case RUNNING:
			return "RUNNING";
		case INACTIVE:
			return "INACTIVE";
		default:
			return "ERROR";
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Status){
			if(((Status) obj).getCode() == this.code) return true;
			else return false;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
		return code;
	}
}
