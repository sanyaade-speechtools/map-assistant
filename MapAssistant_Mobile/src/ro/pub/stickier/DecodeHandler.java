/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.pub.stickier;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.detector.Detector;

import ro.pub.stickier.camera.CameraManager;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.Hashtable;

final class DecodeHandler extends Handler {

  private static final String TAG = DecodeHandler.class.getSimpleName();

  //Activity used for communication with her's handle
  private final CaptureActivity activity;
  
  //Reader used to decode
  private final MultiFormatReader multiFormatReader;
  
  //running state of this decode thread
  private boolean running = true;

  DecodeHandler(CaptureActivity activity, Hashtable<DecodeHintType, Object> hints) {
    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(hints);
    this.activity = activity;
  }

  /**
   * Method handles messages obtained
   * 
   */
  @Override
  public void handleMessage(Message message) {
    if (!running) {
      return;
      //if thread is not running do nothing
    }
    switch (message.what) {
      case R.id.decode:
        decode((byte[]) message.obj, message.arg1, message.arg2);
        break;
      case R.id.quit:
    	//quit, set running false  
        running = false;
        Looper.myLooper().quit();
        break;
    }
  }

  /**
   * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
   * reuse the same reader objects from one decode to the next.
   * It sends message to CaptureActivityHandle with the results
   * 
   * @param data   The YUV preview frame.
   * @param width  The width of the preview frame.
   * @param height The height of the preview frame.
   */
  private void decode(byte[] data, int width, int height) {
    long start = System.currentTimeMillis();
    
    Result rawResult = null;
    
    PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(data, width, height);
    
    Log.d("barcode","got data to build bitmap w h" + width + " " + height);
    
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    
    ResultPoint[] points = null;
    DetectorResult detectorResult = null;

   	// hits instead of null
    try{
    detectorResult = new Detector(bitmap.getBlackMatrix()).detect(null);
    //decoderResult = decoder.decode(detectorResult.getBits(), hints);
    
  //get points
    points = detectorResult.getPoints();
    }
    catch(Exception e){
    	
    	Log.d(TAG,"Exception in detecting 3 QR code corners");	
    
    }
        
/*
 * Don't need all this info    
    try {
      rawResult = multiFormatReader.decodeWithState(bitmap);
    } catch (ReaderException re) {
      // continue
    } finally {
      multiFormatReader.reset();
    }
*/
    
    
    if (points != null) {
        // Don't log the barcode contents for security.
        long end = System.currentTimeMillis();
        Log.d(TAG, "Found corners in " + (end - start) + " ms");
       
        //send result to activity handler 
        Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, detectorResult);
        //Bundle bundle = new Bundle();
        
        //bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
        
        //message.setData(bundle);
        message.sendToTarget();
      } else {
      	
        //sends message to activity handler that decode failed
        Message message = Message.obtain(activity.getHandler(), R.id.decode_failed);
        message.sendToTarget();
      }
 
  }
  

}
