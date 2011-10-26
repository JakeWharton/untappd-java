package com.jakewharton.untappd.services;

import com.google.gson.reflect.TypeToken;
import com.jakewharton.untappd.UntappdApiBuilder;
import com.jakewharton.untappd.UntappdApiService;
import com.jakewharton.untappd.entities.Brewery;
import com.jakewharton.untappd.entities.Response;

public class SearchService extends UntappdApiService {
	public BreweriesBuilder breweries(String query) {
		return new BreweriesBuilder(this).query(query);
	}
	
	public static final class BreweriesBuilder extends UntappdApiBuilder<Response<Brewery>> {
		private static final String URI = "/v3/brewery_search";
		private static final String QUERY = "q";
		
		private BreweriesBuilder(SearchService service) {
			super(service, new TypeToken<Response<Brewery>>() {}, URI);
		}
		
		public BreweriesBuilder query(String query) {
			this.parameter(QUERY, query);
			return this;
		}
	}
}
