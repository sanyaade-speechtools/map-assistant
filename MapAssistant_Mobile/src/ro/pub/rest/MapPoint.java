package ro.pub.rest;


public class MapPoint {

	public float refx;
	
	public float refy;
	
	public String pointName;
	
	public MapPoint(){}
	
	public MapPoint(String name, float x, float y){		
		super();
		this.refx = refx;
		this.refy = refy;	
		this.pointName = name;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return pointName;
	}
}
