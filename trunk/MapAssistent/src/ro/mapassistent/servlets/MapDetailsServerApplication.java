package ro.mapassistent.servlets;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import ro.mapassistent.MapDetailsServerResource;
import ro.mapassistent.TestResource;

public class MapDetailsServerApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.attachDefault(new Directory(getContext(), "war:///"));
		router.attach("/poli", MapDetailsServerResource.class);
		//router.attachDefault(TestResource.class);
		
		return router;
	}

}
