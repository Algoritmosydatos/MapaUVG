package com.uvg.mapa;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity implements LocationListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	
	LocationManager locationManager;
	
	MainPanel mainPanel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mainPanel = new MainPanel(this.getApplicationContext());
        //set our MainGamePanel as the View
        setContentView(mainPanel);
        
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mainPanel.update(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		mainPanel.setOnline(false);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		mainPanel.setOnline(true);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
