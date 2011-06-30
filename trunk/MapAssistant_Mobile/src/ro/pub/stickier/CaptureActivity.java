package ro.pub.stickier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import ro.pub.database.DBHelper;
import ro.pub.rest.MapDetails;
import ro.pub.rest.MapDetailsResource;
import ro.pub.rest.MapPoint;
import ro.pub.rest.RequestToServer;
import ro.pub.stickier.camera.CameraManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.DetectorResult;

public class CaptureActivity extends Activity implements SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private ViewfinderView viewfinderView;

	private CaptureActivityHandler handler;

	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	// private HistoryManager historyManager;
	private Result lastResult;
	private String characterSet;

	private Toast toast;
	// menu option identifiers
	private static final int downloadMap = 1;
	
	private static final int findPlaceDialog = 2;

	private MapDetails mapDetails;

	private ProgressDialog downloadMapDialog;

	private CameraOverlay resultPoints;

	private Dialog findPlaceDialogBox;
	
	private MapPointsAdapter adapter;
	
	private MapPoint destinationPoint;
	
	private DBHelper database;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// init camera manager
		CameraManager.init(getApplication());
		hasSurface = false;
		handler = null;

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		resultPoints = (CameraOverlay) findViewById(R.id.result_points_view);
		
		destinationPoint = new MapPoint("default",0.0f,0.0f);
		
		//database = new DBHelper(this);
		
	}
	

	@Override
	public void onResume() {
		super.onResume();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.camera_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						characterSet);
			}
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit("IOException");
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializating camera", e);
			displayFrameworkBugMessageAndExit("RuntimeException");
		}
	}

	public Handler getHandler() {
		return handler;
	}

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public void handleDecode(DetectorResult points) {
		
		float[] newPoints = new float[8];
		int i = 0;
		for (ResultPoint point : points.getPoints()) {
			newPoints[i] = point.getX();
			i++;
			newPoints[i] = point.getY();
			i++;
		}
		resultPoints.setResultPoints(newPoints);
		resultPoints.setDestination(destinationPoint.refx,destinationPoint.refy);
		resultPoints.invalidate();

		// handleDecodeInternally(rawResult, resultHandler, barcode);
		if (handler != null) {
			handler.sendEmptyMessage(R.id.restart_preview);
		}

	}

	private void displayFrameworkBugMessageAndExit(String info) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("[" + info + "] "
				+ getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void drawViewfinder() {
		// Draw image result

		viewfinderView.drawViewfinder();
	}

	public void removePoints() {

		// remove the points and the drowing
		resultPoints.setResultPoints(null);
		resultPoints.setDestination(0f, 0f);
		resultPoints.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, CaptureActivity.downloadMap, Menu.NONE,
				"Download map").setIcon(android.R.drawable.btn_dropdown);

		menu.add(Menu.NONE,CaptureActivity.findPlaceDialog,Menu.NONE,"Find place").setIcon(android.R.drawable.ic_media_next);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {

		case downloadMap: {

			new GetMapDetails().execute("");

		}
			break;

		case findPlaceDialog : {
			
			
			
			
			if(mapDetails != null)
			{
			
			
			
			findPlaceDialogBox = new Dialog(this);
			
			findPlaceDialogBox.setContentView(R.layout.findplacedialog);
			
			findPlaceDialogBox.setTitle("Chose destination on map");
			
			findPlaceDialogBox.setCancelable(true);
			
			Button buttonClose = (Button) findPlaceDialogBox.findViewById(R.id.closeDialogButton);
			
			buttonClose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					findPlaceDialogBox.dismiss();
					
				}
			});
			
			Button selectButton = (Button) findPlaceDialogBox.findViewById(R.id.selectDialogButton);
			
			selectButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					findPlaceDialogBox.dismiss();
					
				}
			});
			
			
			Spinner spinner = (Spinner) findPlaceDialogBox.findViewById(R.id.dialogSelectDestinationSpinner);
	
			adapter = new MapPointsAdapter(this, android.R.layout.simple_spinner_dropdown_item,mapDetails.mapPoints);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spinner.setAdapter(adapter);
					
			MyOnItemSelectedListener spinnerListener = new MyOnItemSelectedListener();
			
			spinner.setOnItemSelectedListener(spinnerListener);
			
			findPlaceDialogBox.show();	
			}
			else
				 Toast.makeText(this, "Please download map first", Toast.LENGTH_LONG).show();
					      
			
			
				}break;
		}
		return true;
	}

	private class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	
	      MapPoint point = (MapPoint)parent.getItemAtPosition(pos);
	      Toast.makeText(parent.getContext(), "Point is " +
	      point.pointName, Toast.LENGTH_LONG).show();
	      
	      destinationPoint = point;
	      
	      
	    }
	    
	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
	
	private class MapPointsAdapter extends ArrayAdapter<MapPoint>{
		
		private ArrayList<MapPoint> items;
		private Context context;
		
		public MapPointsAdapter(Context context, int textViewResourceId, ArrayList<MapPoint> items) {
				super(context, textViewResourceId, items);
				
			// TODO Auto-generated constructor stub
			this.context = context;
			Log.d("server","Items addes to adapter + " + items.size());
			
			this.items = items;		
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
	
			
			MapPoint point = items.get(position);
			TextView view = new TextView(context);
			view.setText(point.pointName);
			Log.d("server","Text" + view.toString());
			return 	view;
		}	
	}
	
	
	//Download map details
	private class GetMapDetails extends AsyncTask<String, Void, MapDetails> {

		@Override
		protected MapDetails doInBackground(String... params) {

			Log.d("server", "Start process of downloading map points");

			try{
			ClientResource cr = new ClientResource(
					"http://map-assistant.appspot.com/mapdownload/poli");

			cr.setRequestEntityBuffering(true);
			cr.setResponseEntityBuffering(true);

			// Request request = new Request(Method.POST,
			// "http://map-assistant.appspot.com/mapdownload/poli");

			// cr.setRequest(request);
			MapDetailsResource resource = cr.wrap(MapDetailsResource.class);

			Gson gson = new Gson();
			RequestToServer req = new RequestToServer();

			req.requestName = "poli";

			Representation entity = new JsonRepresentation(gson.toJson(req,
					RequestToServer.class));

			String str = resource.getMapDetails(entity);

			Log.d("server", "String " + str);

			mapDetails = gson.fromJson(str, MapDetails.class);

			}catch(Exception e){
				Log.d("server","Connection exception  " + e.getMessage());
			}
			if (mapDetails != null) {
				Log.d("server", "map " + mapDetails.mapName + " "
						+ mapDetails.mapPoints.toString());

			}
			
			return mapDetails;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

				downloadMapDialog = ProgressDialog.show(CaptureActivity.this,
						"", "Downloading map. Please wait...", true);
		}

		@Override
		protected void onPostExecute(MapDetails result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			downloadMapDialog.dismiss();
			Context context = getApplicationContext();
			CharSequence text;
			
			if (result != null)
				text = "Downloaded map " + result.mapName;
			else
				text = "Error in download";

			int duration = Toast.LENGTH_SHORT;
		
			toast = Toast.makeText(context, text, duration);
			toast.show();

		}

	}

}
