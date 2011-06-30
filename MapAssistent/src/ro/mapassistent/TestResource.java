package ro.mapassistent;

import java.io.IOException;
import java.util.Date;

import org.restlet.data.MediaType;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestResource extends ServerResource{

 @Get("xml")
  public Representation represent(){
  // Generate the right representation according to its media type.
          try {
           DomRepresentation domRep = new DomRepresentation(MediaType.TEXT_XML);
              // Generate a DOM document representing the list of
              // items.
              Document d = domRep.getDocument();
              Element r = d.createElement("expire_date");
              d.appendChild(r);
               Element date = d.createElement("date");
               date.appendChild(d.createTextNode(new Date().toLocaleString()));
               r.appendChild(date);
              d.normalizeDocument();
  
              // Returns the XML representation of this document.
              return domRep;
          } catch (IOException e) {
              e.printStackTrace();
          }
  return null;
 }
 
 @Post
  public Representation acceptRepresentation(Representation entity){
  //Could process some post requests here and possibly write to the datastore
  return null;
 }
}