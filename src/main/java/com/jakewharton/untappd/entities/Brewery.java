package com.jakewharton.untappd.entities;

import com.jakewharton.untappd.UntappdEntity;

public class Brewery implements UntappdEntity {
	private static final long serialVersionUID = -3884545985841628897L;
	
	private int brewery_id;
	private String brewery_name;
	private String brewery_stamp;
	private String country_name;
	
	public int getId() {
		return this.brewery_id;
	}
	public String getName() {
		return this.brewery_name;
	}
	public String getStamp() {
		return this.brewery_stamp;
	}
	public String getCountryName() {
		return this.country_name;
	}
}
