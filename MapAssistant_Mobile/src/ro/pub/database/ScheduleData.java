package ro.pub.database;

public class ScheduleData {

	public int sql_id;
	
	public String placeToBe;
	
	public String description;
	
	public int hour;
	
	public int minute;
	
	
	public ScheduleData(){
		
	}
	
	public ScheduleData(String placeToBe, String description, int hour, int minute) {
		super();
		this.placeToBe = placeToBe;
		this.description = description;
		this.hour = hour;
	};
	
	
	

	
}
