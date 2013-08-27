package com.uvg.mapa;

import com.uvg.mapa.gui.PersonalIdentifier;

import android.content.Context;
import android.graphics.Canvas;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainPanel extends SurfaceView implements SurfaceHolder.Callback{
	
	private static final String TAG = MainPanel.class.getSimpleName();
	
	private MainThread thread;
	
	private boolean locationChanged;
	
	//Models
	private PersonalIdentifier id;
	
	public MainPanel(Context context, AttributeSet attr){
		super(context, attr);
		
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		locationChanged = true;
		
	}
	
	public MainPanel(Context context){
		super(context);
		
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
	
	public void render(Canvas canvas){
	}
	
	public void update(Location location) {
		if(locationChanged)
			id.update(location.getLatitude(), location.getLongitude());
	}
	
	public void setOnline(boolean isOnline){
		id.setOnline(isOnline);
	}
	
	public boolean isOnline(){
		return id.isOnline();
	}
	
	public boolean isLocationChanged() {
		return locationChanged;
	}

	public void setLocationChanged(boolean locationChanged) {
		this.locationChanged = locationChanged;
	}
	
}