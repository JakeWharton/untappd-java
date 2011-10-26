package com.jakewharton.untappd;

import java.util.Date;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.apibuilder.ApiBuilder;
import com.jakewharton.apibuilder.ApiException;

/**
 * Trakt-specific API builder extension which provides helper methods for
 * adding fields, parameters, and post-parameters commonly used in the API.
 *
 * @param <T> Native class type of the HTTP method call result.
 * @author Jake Wharton <jakewharton@gmail.com>
 */
public abstract class UntappdApiBuilder<T> extends ApiBuilder {
	private static final String PARAM_API_KEY = "key";

	/** Trakt API URL base. */
	private static final String BASE_URL = "http://api.untappd.com";

	/** Number of milliseconds in a single second. */
	/*package*/ static final long MILLISECONDS_IN_SECOND = 1000;


	/** Valid HTTP request methods. */
	protected static enum HttpMethod {
		Get, Post
	}


	/** Service instance. */
	private final UntappdApiService service;

	/** Type token of return type. */
	private final TypeToken<T> token;

	/** HTTP request method to use. */
	private final HttpMethod method;

	/** String representation of JSON POST body. */
	private JsonObject postBody;


	/**
	 * Initialize a new builder for an HTTP GET call.
	 *
	 * @param service Service to bind to.
	 * @param token Return type token.
	 * @param methodUri URI method format string.
	 */
	public UntappdApiBuilder(UntappdApiService service, TypeToken<T> token, String methodUri) {
		this(service, token, methodUri, HttpMethod.Get);
	}

	/**
	 * Initialize a new builder for the specified HTTP method call.
	 *
	 * @param service Service to bind to.
	 * @param token Return type token.
	 * @param urlFormat URL format string.
	 * @param method HTTP method.
	 */
	public UntappdApiBuilder(UntappdApiService service, TypeToken<T> token, String urlFormat, HttpMethod method) {
		super(BASE_URL + urlFormat);

		this.service = service;

		this.token = token;
		this.method = method;
		this.postBody = new JsonObject();

		this.parameter(PARAM_API_KEY, this.service.getApiKey());
	}


	/**
	 * Execute remote API method and unmarshall the result to its native type.
	 *
	 * @return Instance of result type.
	 * @throws ApiException if validation fails.
	 */
	public final T fire() {
		this.preFireCallback();

		try {
			this.performValidation();
		} catch (Exception e) {
			throw new ApiException(e);
		}

		T result = this.service.unmarshall(this.token, this.execute());
		this.postFireCallback(result);

		return result;
	}

	/**
	 * Perform any required actions before validating the request.
	 */
	protected void preFireCallback() {
		//Override me!
	}

	/**
	 * Perform any required validation before firing off the request.
	 */
	protected void performValidation() {
		//Override me!
	}

	/**
	 * Perform any required actions before returning the request result.
	 *
	 * @param result Request result.
	 */
	protected void postFireCallback(T result) {
		//Override me!
	}

	/**
	 * <p>Execute the remote API method and return the JSON object result.<p>
	 *
	 * <p>This method can be overridden to select a specific subset of the JSON
	 * object. The overriding implementation should still call 'super.execute()'
	 * and then perform the filtering from there.</p>
	 *
	 * @return JSON object instance.
	 */
	protected final JsonElement execute() {
		String url = this.buildUrl();
		while (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}

		try {
			switch (this.method) {
				case Get:
					return this.service.get(url);
				case Post:
					return this.service.post(url, this.postBody.toString());
				default:
					throw new IllegalArgumentException("Unknown HttpMethod type " + this.method.toString());
			}
		} catch (ApiException ae) {
			throw new RuntimeException(ae);
		}
	}

	/**
	 * Print the HTTP request that would be made
	 */
	public final void print() {
		this.preFireCallback();

		try {
			this.performValidation();
		} catch (Exception e) {
			throw new ApiException(e);
		}

		String url = this.buildUrl();
		while (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}

		System.out.println(this.method.toString().toUpperCase() + " " + url);
		for (String name : this.service.getRequestHeaderNames()) {
			System.out.println(name + ": " + this.service.getRequestHeader(name));
		}

		switch (this.method) {
			case Post:
				System.out.println();
				System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(this.postBody));
				break;
		}
	}

	/**
	 * Set the API key.
	 *
	 * @param apiKey API key string.
	 * @return Current instance for builder pattern.
	 */
	/*package*/ final ApiBuilder api(String apiKey) {
		return this.field(PARAM_API_KEY, apiKey);
	}

	/**
	 * Add a URL parameter value.
	 *
	 * @param name Name.
	 * @param value Value.
	 * @return Current instance for builder pattern.
	 */
	protected final ApiBuilder parameter(String name, Date value) {
		return this.parameter(name, Long.toString(UntappdApiBuilder.dateToUnixTimestamp(value)));
	}

	/**
	 * Add a URL parameter value.
	 *
	 * @param name Name.
	 * @param value Value.
	 * @return Current instance for builder pattern.
	 */
	protected final <K extends UntappdEnumeration> ApiBuilder parameter(String name, K value) {
		if ((value == null) || (value.toString() == null) || (value.toString().length() == 0)) {
			return this.parameter(name, "");
		} else {
			return this.parameter(name, value.toString());
		}
	}

	/**
	 * Add a URL field value.
	 *
	 * @param name Name.
	 * @param value Value.
	 * @return Current instance for builder pattern.
	 */
	protected final <K extends UntappdEnumeration> ApiBuilder field(String name, K value) {
		if ((value == null) || (value.toString() == null) || (value.toString().length() == 0)) {
			return this.field(name);
		} else {
			return this.field(name, value.toString());
		}
	}

	protected final boolean hasPostParameter(String name) {
		return this.postBody.has(name);
	}

	protected final UntappdApiBuilder<T> postParameter(String name, String value) {
		this.postBody.addProperty(name, value);
		return this;
	}

	protected final UntappdApiBuilder<T> postParameter(String name, int value) {
		return this.postParameter(name, Integer.toString(value));
	}

	protected final <K extends UntappdEnumeration> UntappdApiBuilder<T> postParameter(String name, K value) {
		if ((value != null) && (value.toString() != null) && (value.toString().length() > 0)) {
			return this.postParameter(name, value.toString());
		}
		return this;
	}

	protected final UntappdApiBuilder<T> postParameter(String name, JsonElement value) {
		this.postBody.add(name, value);
		return this;
	}

	/**
	 * Convert a {@link Date} to its Unix timestamp equivalent.
	 *
	 * @param date Date value.
	 * @return Unix timestamp value.
	 */
	protected static final long dateToUnixTimestamp(Date date) {
		return date.getTime() / MILLISECONDS_IN_SECOND;
	}
}
