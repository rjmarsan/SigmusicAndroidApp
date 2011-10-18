package processing.android.test.sigmusic;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import processing.core.PApplet;
import processing.core.PFont;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.MTManager;
import com.rj.processing.mt.Point;
import com.rj.processing.mt.TouchListener;


public class sigmusic extends PApplet implements TouchListener {
	private final String TAG = "YayProcessingPD";
	public int sketchWidth() { return this.screenWidth; }
	public int sketchHeight() { return this.screenHeight; }
	public String sketchRenderer() { return PApplet.P2D; }
	
	MTManager manager;
	PFont font;
	@Override
	public void setup() {
		Log.d(TAG, "Setting up");
		  colorMode(HSB);
		openPatch("RythemPatch.pd");
		manager = new MTManager();
		manager.addTouchListener(this);
		font = createFont("americantypewriter", 42, true);
		this.textFont(font);
	}
	
	float x = 0;
	float animate_color = 0;
	float animate_color_diff = 1;
	float distance = 0;

	
	@Override
	public boolean surfaceTouchEvent(MotionEvent event) {
		manager.surfaceTouchEvent(event);
		return super.surfaceTouchEvent(event);
	}
	
	
	
	
	@Override
	public void draw() {
	  background(0);
	  Cursor cur1 = this.cur1;
	  Cursor cur2 = this.cur2;
	  
	  rectMode(CENTER);
	  textMode(SCREEN);
	  ellipseMode(CENTER);
	  fill(animate_color,255,255);
	  stroke(animate_color,255,255);

	  //no points
	  if (cur1 == null) {
		  textAlign(CENTER, CENTER);
		  text("Touch the screen\n   to be awesome.\nTurn the volume up\n   for more awesome.", width/2,height/2);
	  } else {
		  rect(cur1.currentPoint.x,cur1.currentPoint.y,150,150);
		  if (cur2 != null) {
			  line(cur1.currentPoint.x,cur1.currentPoint.y,cur2.currentPoint.x,cur2.currentPoint.y);
			  ellipse(cur2.currentPoint.x,cur2.currentPoint.y,150,150);
		  }
	  }
	  animate_color += animate_color_diff;
	  animate_color = animate_color % 255;
	  

	}
	
	
	
	
	
	/**
	 * Pure Data stuff
	 */

	private static final int SAMPLE_RATE = 44100;

	private Toast toast = null;
	
	private void toast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
				}
				toast.setText(TAG + ": " + msg);
				toast.show();
			}
		});
	}

	public void onResume() {
		super.onResume();
		Log.d(TAG, "Starting LibPD");
		if (AudioParameters.suggestSampleRate() < SAMPLE_RATE) {
			toast("required sample rate not available; exiting");
			finish();
			return;
		}
		final int nOut = Math.min(AudioParameters.suggestOutputChannels(), 2);
		if (nOut == 0) {
			toast("audio output not available; exiting");
			finish();
			return;
		}
		try {
			PdAudio.initAudio(SAMPLE_RATE, 0, nOut, 1, true);
			PdAudio.startAudio(this);
			} catch (final IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public void onPause() {
		super.onPause();
		PdAudio.stopAudio();
	}
	
	public void onDestroy() {
		cleanup();
		super.onDestroy();
	}
	
	public void finish() {
		Log.d(TAG, "Finishing for some reason");
		cleanup();
		super.finish();
	}

	
	public int openPatch(final String patch) {
		final File dir = this.getFilesDir();
		final File patchFile = new File(dir, patch);
		int out=-1;
		try {
			IoUtils.extractZipResource(this.getResources().openRawResource(processing.android.test.sigmusic.R.raw.patch), dir, true);
			out = PdBase.openPatch(patchFile.getAbsolutePath());
		} catch (final IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.toString() + "; exiting now");
			finish();
		}
		return out;
	}		

	public void cleanup() {
		// make sure to release all resources
		PdAudio.stopAudio();
		PdBase.release();
	}
	
	
	Cursor cur1;
	Cursor cur2;
	
	@Override
	public void touchDown(Cursor c) {
		Log.d("Sigmusic", "Touch down:"+manager.cursors.size());
		if (c.curId == 0) {
			cur1 = c;
		} else if (c.curId == 1) {
			cur2 = c;
		}
		PdBase.sendBang("start");
		updateTouches();
	}
	@Override
	public void touchUp(Cursor c) {
		if (c == cur2) cur2 = null;
		if (c == cur1) cur1 = null;
		distance = 0;
	}
	@Override
	public void touchMoved(Cursor c) {
		updateTouches();
	}
	
	void updateTouches() {
		if (cur1 == null) return; // what? how did we get here?
		float xval = (float) cur1.currentPoint.x / (float) width;
		animate_color_diff = (1-xval)*2;
		xval = 20 + (float) Math.pow(1.8, xval * 12f);
		Log.d("Sigmusic", "Xval:" + xval);
		float yval = (float) cur1.currentPoint.y / (float) height;
		yval = 50 + yval * 40;
		
		if (cur2 != null) {
			distance = Point.distance(cur1.currentPoint, cur2.currentPoint);
			float dist = distance / width;
			dist = dist * 50;
			PdBase.sendFloat("separation", dist);
		}
		
		PdBase.sendFloat("tempo", xval);
		PdBase.sendFloat("pitch", yval);
	}
	@Override
	public void touchAllUp(Cursor c) {
		PdBase.sendBang("stop");
		cur1 = null;
		cur2 = null;
	}

}