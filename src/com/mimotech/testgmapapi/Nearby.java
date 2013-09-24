package com.mimotech.testgmapapi;

import java.util.ArrayList;

public class Nearby {
	//Bangkok as default
	public String title ;
	public String lat ;
	public String lng ;
	public static ArrayList<Camera> camList;
	public Nearby(String title,String lat,String lng){
		this.title = title;
		this.lat = lat;
		this.lng = lng;
	}

	
}
