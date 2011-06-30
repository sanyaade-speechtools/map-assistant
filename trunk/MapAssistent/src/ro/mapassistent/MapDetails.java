package ro.mapassistent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Id;


public class MapDetails implements Serializable{

	
	@Id Long id;
	
	@Embedded public List<MapPoint> mapPoints = new ArrayList<MapPoint>();
	
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
	
	
	private void readObject(
		     ObjectInputStream aInputStream
		   ) throws ClassNotFoundException, IOException {
		     //always perform the default de-serialization first
		try {
			

	        mapPoints = (ArrayList<MapPoint>) aInputStream.readObject();
	} catch (IOException e) {
	        e.printStackTrace();
	} catch (ClassNotFoundException e) {
	        e.printStackTrace();
	}


		    
		  }

		    /**
		    * This is the default implementation of writeObject.
		    * Customise if necessary.
		    */
		    private void writeObject(
		      ObjectOutputStream aOutputStream
		    ) throws IOException {
		      //perform the default serialization for all non-transient, non-static fields
		    	ByteArrayOutputStream bos = null;
		    	try {
		    	        
		    	        
		    	        
		    	        aOutputStream.writeObject(mapName);
		    	        //aOutputStream.writeObject(mapPoints);
		    	        
		    	        int i=0;
		    	        for(i=0;i<mapPoints.size();i++)
		    	        	aOutputStream.writeObject(mapPoints.get(i));
		    	} catch (IOException e) {
		    	        e.printStackTrace();
		    	}

		    }
}
