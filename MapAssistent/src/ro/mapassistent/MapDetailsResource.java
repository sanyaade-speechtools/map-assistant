package ro.mapassistent;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;




public interface MapDetailsResource {
	@Get("xml")
	public Representation retrieve();
	
	@Post("xml")
	public Representation getMap(String name);
	
	@Post("json")
	public String getMapDetails(Representation entity);
	
	@Put
	public void store(MapDetails mapDetails);
	
	@Delete
	public void remove();
	

}
