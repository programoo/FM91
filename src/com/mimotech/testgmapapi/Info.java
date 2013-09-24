package com.mimotech.testgmapapi;

import java.util.ArrayList;

public class Info {
	//Bangkok as default
	public static double lat = 13.724714;
	public static double lng = 100.633111;
	public static String reverseGpsName="";
	public static ArrayList<Camera> camList;
	public static final int RESULT_OK = 500;
	public static final int RESULT_CANCELED = 600;

	public Info(){
		
	}
	
	public double distance(double lat1, double lon1, double lat2, double lon2,
			String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit.equalsIgnoreCase("K")) {
			dist = dist * 1.609344;
		} else if (unit.equalsIgnoreCase("N")) {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static Camera getCamById(String id) {
		for (int i = 0; i < Info.camList.size(); i++) {
			if (Info.camList.get(i).id.equalsIgnoreCase(id)) {
				return Info.camList.get(i);
			}
		}
		return null;
	}

	
}
