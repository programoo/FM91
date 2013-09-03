package com.mimotech.testgmapapi;

public class Camera {
	public String id;
	public String englishName;
	public String thaiName;
	public String lat;
	public String lng;
	public String updateStatus;
	public String imgUrl;
	public String lastUpdate;
	public String image;
	public String source;
	public String camList;

	public Camera(String id, String englishName, String thaiName, String lat,
			String lng, String updateStatus,String imgUrl, String lastUpdate, String image,
			String source, String camList) {
		this.id = id;
		this.englishName = englishName;
		this.thaiName = thaiName;
		this.lat = lat;
		this.lng = lng;
		this.updateStatus = updateStatus;
		this.imgUrl = imgUrl;
		this.lastUpdate = lastUpdate;
		this.image = image;
		this.source = source;
		this.camList = camList;
	}
}
