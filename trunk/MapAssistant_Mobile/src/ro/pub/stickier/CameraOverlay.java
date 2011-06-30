package ro.pub.stickier;

import org.restlet.resource.ClientResource;

import ro.pub.rest.MapDetails;
import ro.pub.rest.MapDetailsResource;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CameraOverlay extends View {

	private static final String TAG = CameraOverlay.class.getSimpleName();
	
	private float[] points;
	
	
	private PointF topLeft;
	
	private PointF topRight;
	
	private PointF bottomLeft;
	
	float destinationRelativeX;
	
	float destionationRelativeY;
	
	
	PointF destionation;
	public CameraOverlay(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	public CameraOverlay(Context context,AttributeSet attr) {
		super(context,attr);
		// TODO Auto-generated constructor stub
	}
	
	
	
	private void setDestinationOffsets(float destionationRelativeX, float destiontionRelativeY){
		
	this.destinationRelativeX = destionationRelativeX;
	
	this.destionationRelativeY = destiontionRelativeY;
	}
	private double distance(PointF a, PointF b){
		
		double dist = 0;
		
		dist = Math.sqrt((double)Math.abs(a.x - b.x) + (double)Math.abs(a.y - b.y));
		
		return dist;
	}
	private void settleMapCoordinates(){
		
		if(points != null){
	
			//logic here determin witch point is witch
			
			
			bottomLeft = new PointF(points[0],points[1]);
			topLeft = new PointF(points[2],points[3]);
			topRight = new PointF(points[4],points[5]);

		}
		else
		{
			Log.d(TAG,"Point is null, no data for map orientation");
			
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	
		int i;
		if(points == null)
			return;
		
		if (points != null && points.length > 0) {
			
			Paint paint = new Paint();
			
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			
			paint.setStyle(Paint.Style.STROKE);

			paint.setColor(getResources().getColor(R.color.result_points));
			
				paint.setStrokeWidth(10.0f);
				for (i=0;i<points.length;i+=2) {
					canvas.drawPoint(points[i], points[i+1], paint);
					Log.d(TAG,"Points " + i +" "+ points[i] + " " + points[i+1] );
					
				}
				
			paint.setColor(getResources().getColor(R.color.destionation_color));
			
			// color destionation
			if(destionation != null){
				
			//canvas.drawPoint(destionation.x,destionation.y, paint);
			
			paint.setStrokeWidth(3.0f);
			
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(destionation.x, destionation.y, 5f, paint);
			}
		}	
	
	}
	
	public void setResultPoints(float[] resultPoints){
	
		this.points = resultPoints;
		//determine witch point is witch
		settleMapCoordinates();
		
	}
	
	//set the desired destionation relative to points 
	public void setDestination(float dx, float dy){
		
		destinationRelativeX = dx;
		destionationRelativeY = dy;
		
		if(dx != 0 || dy !=0){
					
		float offx = topRight.x - topLeft.x;
		float offy = topRight.y - topLeft.y;
	
		destionation = new PointF( topLeft.x + (topRight.x - topLeft.x)*dx + (bottomLeft.x - topLeft.x) * dy,topLeft.y + (topRight.y - topLeft.y)*dx + (bottomLeft.y - topLeft.y) * dy);
		
		}
		else
			destionation = null;
	}
	
}
