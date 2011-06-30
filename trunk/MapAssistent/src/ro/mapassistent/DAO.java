package ro.mapassistent;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.helper.DAOBase;




public class DAO extends DAOBase{

	static {
        /**Classes registration goes here */
		ObjectifyService.register(MapDetails.class);
		//Embedded class
		//ObjectifyService.register(MapPoint.class);
        
    }


}
