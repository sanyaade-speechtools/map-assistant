package ro.pub.rest;

public class RequestToServer {

public final int MAP_REQUEST = 1;
	
	//other requests.
	
	// in our case map name
	public String requestName;
	
	// query server for map id
	public Long RequestId;
	
	public int RequestType;

	public RequestToServer(){
		
	}
	
}
