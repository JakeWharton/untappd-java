package com.jakewharton.untappd.entities;

import java.util.List;

import com.jakewharton.untappd.UntappdEntity;

public class Response<T extends UntappdEntity> implements UntappdEntity {
	private static final long serialVersionUID = -1231543243827248375L;

	public int http_code;
	public int returned_results;
	public List<T> results;
	
	public int getHttpCode() {
		return this.http_code;
	}
	public int getReturnedResults() {
		return this.returned_results;
	}
	public List<T> getResults() {
		return this.results;
	}
}
