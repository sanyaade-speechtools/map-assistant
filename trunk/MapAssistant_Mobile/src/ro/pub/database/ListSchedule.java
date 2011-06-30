package ro.pub.database;

import java.util.ArrayList;

import ro.pub.stickier.R;

import android.app.ListActivity;
import android.os.Bundle;

public class ListSchedule extends ListActivity{
	
	
	private ArrayList<ScheduleData> schedule;
	
	private DBHelper database;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.schedule_list_info);
		
		database = new DBHelper(this);
		
		schedule = database.getAllScheduleData();
		
	}

}
