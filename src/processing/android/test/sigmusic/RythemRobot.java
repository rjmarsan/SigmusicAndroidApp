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


public class RythemRobot extends PApplet implements TouchListener {
	private final String TAG = "RythemRobot";
	public int sketchWidth() { return this.screenWidth; }
	public int sketchHeight() { return this.screenHeight; }
	public String sketchRenderer() { return PApplet.P2D; }
	
	MTManager manager;
	PFont font;
	@Override
	public void setup() {
		Log.d(TAG, "Setting up");
		manager = new MTManager();
		manager.addTouchListener(this);
		processingSetup();
		pdSetup();
	}
	
	void processingSetup() {
		colorMode(HSB);
		font = createFont("americantypewriter", 42, true);
		this.textFont(font);
	}
	
	void pdSetup() {
		openPatch("RythemRobot.pd");
	}

	
	@Override
	public boolean surfaceTouchEvent(MotionEvent event) {
		manager.surfaceTouchEvent(event);
		return super.surfaceTouchEvent(event);
	}
	
	
	
	
	@Override
	public void draw() {
	  background(0);
	}
	
	
	
	
	
	
	@Override
	public void touchDown(Cursor c) {
	}
	@Override
	public void touchUp(Cursor c) {
	}
	@Override
	public void touchMoved(Cursor c) {
	}

	@Override
	public void touchAllUp(Cursor c) {
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
	
}