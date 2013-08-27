package com.uvg.mapa.gui;

public class BuildingLocation {
	
	float lr_lat, lr_long, ul_lat, ul_long;
	int id;
	
	public BuildingLocation(int id, float lr_lat, float lr_long, float ul_lat, float ul_long){
		this.id = id;
		
		this.lr_lat = lr_lat;
		this.lr_long = lr_long;
		this.ul_lat = ul_lat;
		this.ul_long = ul_long;
	}
	
	public boolean checkCoordinates(float latitude, float longitude){
		if((latitude <= lr_lat && longitude <= lr_long) && (latitude >= ul_lat && longitude >= ul_long))
			return true;
		else
			return false;
	}
	
	
	
}
