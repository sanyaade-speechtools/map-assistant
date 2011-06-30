package ro.mapassistent;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.Gson;

public class MapDetailsServerResource extends ServerResource implements
		MapDetailsResource {

	private volatile MapDetails mapDetails = null;

	private static DAO dao = new DAO();

	public void remove() {
		mapDetails = null;
	}

	public String getMapDetails(Representation entity) {

		
		try {
			  
			  if (entity.getMediaType().equals(MediaType.APPLICATION_JSON,
			    true)) {
			  
				//create json object
				  
				  
					Gson gson = new Gson();

					RequestToServer request = gson.fromJson(entity.getText(),RequestToServer.class);
					
					String repstr = gson.toJson(dao.ofy().query(MapDetails.class)
							.filter("mapName", request.requestName).get());
				  
			  
			   getResponse().setStatus(Status.SUCCESS_OK);
			   // We are setting the representation in this example always to
			   // JSON.
			   // You could support multiple representation by using a
			   // parameter
			   // in the request like "?response_format=xml"
			   
			   return repstr;
			  } else {
			   getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			  }
			 
		}catch(Exception e){
			
			
		}
			
		
		return null;
		
		
		
	}

	public Representation getMap(String name) {

		mapDetails = dao.ofy().query(MapDetails.class).filter("mapName", name)
				.get();

		if (mapDetails == null)
			new MapDetails("error");

		// Generate a DOM document representing the list of
		// items.
		try {
			DomRepresentation domRep = new DomRepresentation(MediaType.TEXT_XML);
			// Generate a DOM document representing the list of
			// items.
			Document d = domRep.getDocument();
			Element map = d.createElement("mapdetails");
			d.appendChild(map);

			Element str = d.createElement("name");

			str.appendChild(d.createTextNode(mapDetails.mapName));

			map.appendChild(str);

			Element list = d.createElement("points");

			if (mapDetails.mapPoints.size() > 0) {
				Element point;
				int i = 0;
				for (i = 0; i < mapDetails.mapPoints.size(); i++) {
					point = d.createElement("point");

					point.appendChild(d.createTextNode(mapDetails.mapPoints
							.get(i).pointName));
					point.appendChild(d.createTextNode(Float
							.toString(mapDetails.mapPoints.get(i).refx)));
					point.appendChild(d.createTextNode(Float
							.toString(mapDetails.mapPoints.get(i).refy)));

					list.appendChild(point);
				}

			}
			map.appendChild(list);

			d.normalizeDocument();

			// Returns the XML representation of this document.
			return domRep;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Returns the XML representation of this document.
		return null;

		// return null;
	}

	public Representation retrieve() {

		mapDetails = dao.ofy().query(MapDetails.class)
				.filter("mapName", "poli").get();

		if (mapDetails == null)
			new MapDetails("error");

		// Generate a DOM document representing the list of
		// items.
		try {
			DomRepresentation domRep = new DomRepresentation(MediaType.TEXT_XML);
			// Generate a DOM document representing the list of
			// items.
			Document d = domRep.getDocument();
			Element map = d.createElement("mapdetails");
			d.appendChild(map);

			Element name = d.createElement("name");

			name.appendChild(d.createTextNode(mapDetails.mapName));

			map.appendChild(name);

			Element list = d.createElement("points");

			if (mapDetails.mapPoints.size() > 0) {
				Element point;
				int i = 0;
				for (i = 0; i < mapDetails.mapPoints.size(); i++) {
					point = d.createElement("point");

					point.appendChild(d.createTextNode(mapDetails.mapPoints
							.get(i).pointName));
					point.appendChild(d.createTextNode(Float
							.toString(mapDetails.mapPoints.get(i).refx)));
					point.appendChild(d.createTextNode(Float
							.toString(mapDetails.mapPoints.get(i).refy)));

					list.appendChild(point);
				}

			}
			map.appendChild(list);

			d.normalizeDocument();

			// Returns the XML representation of this document.
			return domRep;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Returns the XML representation of this document.
		return null;

		// return mapDetails;
	}

	public void store(MapDetails mapDetails) {
		// MapDetailsServerResource.mapDetails = mapDetails;
	}

}
