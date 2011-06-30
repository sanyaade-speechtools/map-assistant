package ro.pub.rest;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;




public interface MapDetailsResource {
	@Get("xml")
	public Representation retrieve();
	
	
	@Post("json:json")
	public String getMapDetails(Representation entity);
	
	@Post("xml")
	public Representation getMap(String name);
	
	@Put
	public void store(MapDetails mapDetails);
	
	@Delete
	public void remove();
	

}
