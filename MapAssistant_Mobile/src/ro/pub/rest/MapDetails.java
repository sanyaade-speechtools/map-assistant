package ro.pub.rest;

import java.util.ArrayList;
import java.util.List;


public class MapDetails {

	
	Long id;
	
	public ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
	
	public String mapName;
	
	//Must have no arg constructor for objectify
	public MapDetails(){}
	

	public MapDetails(String name){
		
		
		mapName = name;
	}
	
	public void addPoint(MapPoint point){
		
		mapPoints.add(point);
	}
	
	public void deletePoints(){
		
		mapPoints.clear();
	}
}
