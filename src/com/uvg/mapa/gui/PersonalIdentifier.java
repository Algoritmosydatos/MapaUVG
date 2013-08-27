package com.uvg.mapa.gui;

import android.graphics.Canvas;
import android.graphics.Paint;

public class PersonalIdentifier {
	
	private static final String TAG = PersonalIdentifier.class.getSimpleName();
	
	private double latitude, longitude;
	
	private float radius;
	
	private boolean online, alphaIncreasing;
	
	private int flashingIntensity;
	
	private Paint paint;
	
	public PersonalIdentifier(double latitude, double longitude, boolean online){
		this.latitude = latitude;
		this.longitude = longitude;
		this.online = online;
		
		this.radius = 3;
		
		paint = new Paint();
		paint.setARGB(0, 255, 0, 0);
		
		flashingIntensity = 0;
		alphaIncreasing = true;
	}
	
	private void flash(){
		if(flashingIntensity > 255){
			flashingIntensity = 0;
			alphaIncreasing = !alphaIncreasing;
		}else{
			flashingIntensity++;
		}
	}
	
	private void updatePaint(){
		flash();
		if(online)
			paint.setARGB(paint.getAlpha(), 255, 0, 0);
		else
			paint.setARGB(paint.getAlpha(), 255, 255, 255);
		
		if(alphaIncreasing)
			paint.setAlpha(flashingIntensity);
		else
			paint.setAlpha(255-flashingIntensity);
	}
	
	// the draw method which draws the corresponding frame
	public void draw(Canvas canvas) {
		// where to draw the sprite
		canvas.drawCircle((float)latitude, (float)longitude, radius, paint);
	}
	
	public void update(double latitude, double longitude){
		updatePaint();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isAlphaIncreasing() {
		return alphaIncreasing;
	}

	public void setAlphaIncreasing(boolean alphaIncreasing) {
		this.alphaIncreasing = alphaIncreasing;
	}

	public int getFlashingIntensity() {
		return flashingIntensity;
	}

	public void setFlashingIntensity(int flashingIntensity) {
		this.flashingIntensity = flashingIntensity;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
}