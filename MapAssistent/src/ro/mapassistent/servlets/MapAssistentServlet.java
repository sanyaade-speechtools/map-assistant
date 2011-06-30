package ro.mapassistent.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;

import ro.mapassistent.DAO;
import ro.mapassistent.MapDetails;
import ro.mapassistent.MapPoint;

@SuppressWarnings("serial")
public class MapAssistentServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/plain");
		resp.getWriter().println("Objectify test");

		// testing objectify

		MapDetails map;
		DAO dao = new DAO();

		map = dao.ofy().query(MapDetails.class).filter("mapName", "poli").get();

		if (map == null) {

			// create politehnica map
			map = new MapDetails("poli");

			MapPoint point = new MapPoint("EC", 0.27f, 0.8f);

			map.addPoint(point);
			point = new MapPoint("ED", 0.30f, 0.52f);
			map.addPoint(point);
			
			
			point = new MapPoint("AN", 0.45f, 0.4f);
			map.addPoint(point);

			point = new MapPoint("BN", 0.45f, 0.3f);
			map.addPoint(point);
			
			point = new MapPoint("A", 0.85f, 1f);
			map.addPoint(point);
			
			dao.ofy().put(map);

		} else
			resp.getWriter().println(
					"Map exists in database" + map.mapName + " "
							+ " no points " + map.mapPoints.size());

		resp.getWriter().println("Map details successfully saved");

	}
}
