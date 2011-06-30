
package ro.pub.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class DBHelper.
 */
public class DBHelper {
  
	
	/** The D b_ prefi x_ name. */
	public static String DB_PREFIX_NAME = "AM_";
	
   /** The String DB_NAME. */
   public static String DB_NAME = "map_assistent";
                                                  
   /** The is named. */
   public static Boolean isDataBaseNamed = false;
   

   /** The Constant DB_TABLE_SCHEDULE_DATA. */
   public	static  final String DB_TABLE_SCHEDULE_DATA = "schedule";
   
   /** The Constant DB_VERSION. */
   public   static  final int DB_VERSION = 3;
   
   

   /** The Constant COLS_FRIENDS_INFO. define field names of table friend info */
private static final String[] COLS_SCHEDULE_DATA = new String[]
     { "_id", "minute", "hour", "placeToBe", "description" };

   /** The db. */
   private SQLiteDatabase db;
   
   /** The db open helper. */
   private DBOpenHelper dbOpenHelper;
   
                                                   
   /**
    * The Class DBOpenHelper.
    */
   private static class DBOpenHelper extends
                                                      
       SQLiteOpenHelper {
       
     
       /** The Constant DB_CREATE_TABLE_FRIENDSINFO. */
       private static final String DB_CREATE_TABLE_SCHEDULE = "CREATE TABLE "
    	   
              + DBHelper.DB_TABLE_SCHEDULE_DATA
                                                                                    
              + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, hour INTEGER, minute INTEGER, placeToBe TEXT, description TEXT);";
       
       /**
        * Instantiates a new dB open helper.
        *
        * @param context the context
        * @param dbName the database name
        * @param version the version
        */
       public DBOpenHelper(Context context, String dbName, int version) {
          super(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
       }
                                                          
       /* (non-Javadoc)
        * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
        */
       @Override
                                                               
       public void onCreate(SQLiteDatabase db) {
          try {
              db.execSQL(DBOpenHelper.DB_CREATE_TABLE_SCHEDULE);
              
              
              Log.d("iii","creare Table schedule data");
              
              Log.d("iii","Baza de date a fost creata");
              
          } catch (SQLException e) {
              Log.d("iii","Baza de date nu a putut fi creata" + e.getMessage());
          }
       }
       
       /* (non-Javadoc)
        * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
        */
       @Override
       public void onOpen(SQLiteDatabase db) {
          super.onOpen(db);
       }
                                                                  
                                                                     
       /* (non-Javadoc)
        * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
        */
       @Override
                                                                     
       public void onUpgrade(SQLiteDatabase db, int oldVersion,
          int newVersion) {
    	   
    	   Log.d("iii","start droping tables.");
    	  // drops databases tables if they exist and calls onCreate 
          db.execSQL("DROP TABLE IF EXISTS " + DBHelper.DB_TABLE_SCHEDULE_DATA);
          
          this.onCreate(db);
       }
   }
   
   
   
   /**
    * Instantiates a new dB helper.
    *
    * @param context the context
    */
   public DBHelper(Context context) {
	      this.dbOpenHelper = new DBOpenHelper(context, "WR_DATA", 1);
	      this.establishDb();
	                                                              
	   }
	                                
	                                                    
	                                         
	                                                                          
	   /**
   	 * Establish db.
   	 */
   	private void establishDb() {                               
	      if (this.db == null) {
	
	          this.db = this.dbOpenHelper.getWritableDatabase();
	       }
	    }
	    
    	/**
    	 * Cleanup.
    	 * Called only once, when exiting activity
    	 */
    	public void cleanup() {           
	                                   
	       if (this.db != null) {
	    	   
	    	   
	    	  isDataBaseNamed = false;
	    	  
	    	  DB_NAME = "";
	          
	    	  this.db.close();
	          this.db = null;
	       }
	    }
    	
    	
   
	    /**
    	 * Insert.
    	 *
    	 * @param friend the friend
    	 */
    	public void insertSchedule(int minute, int hour, String placeToBe, String description) {
	      
    		ContentValues values = new ContentValues();
	    
	       values.put("minute", minute);
	       values.put("hour", hour);
	       values.put("placeToBe", placeToBe);
	       values.put("description", description);
	
	       
	      
	    
	       try{
	       long error = this.db.insertOrThrow(DBHelper.DB_TABLE_SCHEDULE_DATA, null, values);
	       
	       Log.d("iii","Inserare;  este eroare ? "+error);
	       
	       }
	       catch(SQLException e){
	    	   e.printStackTrace();
	    	   
	    	   Log.d("iii","Nu s-a putut insera inregistrarea in baza de date"+ e.getMessage());
	       }
	       Log.d("iii","Inserare friend "+values.toString() );
	    }
    	
    	
    	
    	
    	/**
	     * Update my info.
	     *
	     * @param myInfo the my info
	     * @return the int
	     */
	    public int updateSchedule(ScheduleData schedule) {
 	       ContentValues values = new ContentValues();
 	                                                                                  
 	      
 	       values.put("minute", schedule.minute);
 	       values.put("hour", schedule.hour);
 	       values.put("placeToBe", schedule.placeToBe);
   	       values.put("description", schedule.description);

 	       
 	       
 	       return this.db.update(DBHelper.DB_TABLE_SCHEDULE_DATA, values, "_id="+schedule.sql_id, null);
    	}
    	
	   
    	/**
	     * Delete.
	     *
	     * @param friend the friend
	     */
    	public void deleteScheduleInfo(int id) {
	       this.db.delete(DBHelper.DB_TABLE_SCHEDULE_DATA, "_id='" +id+"'" , null);
	    }
	    
    	
    	/**
	     * Clear friend info table.
	     */
	    public void clearFriendInfoTable() {
 	       int nr;
    		
    		nr = this.db.delete(DBHelper.DB_TABLE_SCHEDULE_DATA, null, null);
 	       
 	       Log.d("iii","deleting " + nr + " records from table" + DB_TABLE_SCHEDULE_DATA);
 	       
 	    }
    	
    	
    /**
     * Gets the all friend requests.
     *
     * @return the all friend requests
     */
    public ArrayList<ScheduleData> getAllScheduleData(){
    	
    	ArrayList<ScheduleData> ret = new ArrayList<ScheduleData>();
    	Cursor c = null;
    	
    	 try {
	         c = this.db.query(DBHelper.DB_TABLE_SCHEDULE_DATA, DBHelper.COLS_SCHEDULE_DATA, null,
	           null, null, null, null);
	         int numRows = c.getCount();
	         
	         Log.d("iii","SQLite : Se returneaza "+numRows+" inregistrari");
	        c.moveToFirst();
	         for (int i = 0; i < numRows; i++) {
	        	 
	           ScheduleData request = new ScheduleData();
	            request.hour = c.getInt(c.getColumnIndex("hour"));
	            request.minute = c.getInt(c.getColumnIndex("minute"));
	            request.placeToBe = c.getString(c.getColumnIndex("placeToBe"));
	            request.description = c.getString(c.getColumnIndex("description"));
	            request.sql_id = c.getInt(c.getColumnIndex("_id"));

	           
	            ret.add(request);
	            c.moveToNext();
	        }
	         
	         Log.d("iii","SQLite : Lista friends requests contine " + ret.size() + "inregistrari");
	     } catch (SQLException e) {
	        
	     } finally {
	        if (c != null && !c.isClosed()) {
	            c.close();
	        }
	     }
	     return ret;
    	
    	
    	
    }
	  
	  
	  /**
  	 * Recreate database.
  	 * Drop existing tables and recreates them 
  	 * !!!!!!!!!!!!!! ALL DATA IN THE DATABASE WILL BE LOST !!!!!!!!!!!!!!
  	 *  
  	 */
  	public void recreateDatabase(){
		  
		  this.dbOpenHelper.onUpgrade(this.db, 1, 2);
	  
	  }
  	
  	/**
	   * Sets the database name.
	   *
	   * @param dataBaseName the new database name
	   */
	  public static void setDatabaseName(String dataBaseName){
  		
  		if(isDataBaseNamed == false)
  			{
  			Log.d("iii","DataBase named :"+ DB_NAME + dataBaseName);
  			DB_NAME =DB_PREFIX_NAME + dataBaseName;
  			isDataBaseNamed = true;
  			}
  		
  	}
	  
	  /**
  	 * Checks if is database name set.
  	 *
  	 * @return the boolean
  	 */
  	public static Boolean isDatabaseNameSet(){
		  
		  return isDataBaseNamed;
	  }
 
}


