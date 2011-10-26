package com.jakewharton.untappd;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.apibuilder.ApiException;
import com.jakewharton.apibuilder.ApiService;
import com.jakewharton.untappd.util.Base64;

/**
 * Untappd-specific API service extension which facilitates provides helper
 * methods for performing remote method calls as well as deserializing the
 * corresponding JSON responses.
 * 
 * @author Jake Wharton <jakewharton@gmail.com>
 */
public abstract class UntappdApiService extends ApiService {
	/** Default connection timeout (in milliseconds). */
	private static final int DEFAULT_TIMEOUT_CONNECT = 60 * (int)UntappdApiBuilder.MILLISECONDS_IN_SECOND;
	
	/** Default read timeout (in milliseconds). */
	private static final int DEFAULT_TIMEOUT_READ = 60 * (int)UntappdApiBuilder.MILLISECONDS_IN_SECOND;
	
	/** HTTP header name for authorization. */
	private static final String HEADER_AUTHORIZATION = "Authorization";
	
	/** HTTP authorization type. */
	private static final String HEADER_AUTHORIZATION_TYPE = "Basic";
	
	/** Character set used for encoding and decoding transmitted values. */
	private static final Charset UTF_8_CHAR_SET = Charset.forName(ApiService.CONTENT_ENCODING);
	
	/** HTTP post method name. */
	private static final String HTTP_METHOD_POST = "POST";
	
	
	/** JSON parser for reading the content stream. */
    private final JsonParser parser;
    
    /** API key. */
    private String apiKey;
	
    
    /**
     * Create a new Untappd service with our proper default values.
     */
	public UntappdApiService() {
		this.parser = new JsonParser();
		
		//Setup timeout defaults
		this.setConnectTimeout(DEFAULT_TIMEOUT_CONNECT);
		this.setReadTimeout(DEFAULT_TIMEOUT_READ);
	}
	
	
	/**
	 * Execute request using HTTP GET.
	 * 
	 * @param url URL to request.
	 * @return JSON object.
	 */
	public JsonElement get(String url) {
		return this.unmarshall(this.executeGet(url));
	}
	
	/**
	 * Execute request using HTTP POST.
	 * 
	 * @param url URL to request.
	 * @param postBody String to use as the POST body.
	 * @return JSON object.
	 */
	public JsonElement post(String url, String postBody) {
		return this.unmarshall(this.executeMethod(url, postBody, null, HTTP_METHOD_POST, HttpURLConnection.HTTP_OK));
	}
	
	/**
	 * Set email and password to use for HTTP basic authentication.
	 * 
	 * @param username Username.
	 * @param password_sha Password SHA1.
	 */
	public void setAuthentication(String username, String password_sha) {
		if ((username == null) || (username.length() == 0)) {
			throw new IllegalArgumentException("Username must not be empty.");
		}
		if ((password_sha == null) || (password_sha.length() == 0)) {
			throw new IllegalArgumentException("Password SHA must not be empty.");
		}
		
		String source = username + ":" + password_sha;
		String authentication = HEADER_AUTHORIZATION_TYPE + " " + Base64.encodeBytes(source.getBytes());
		
		this.addRequestHeader(HEADER_AUTHORIZATION, authentication);
	}
	
	/**
	 * Get the API key.
	 * 
	 * @return Value
	 */
	/*package*/ String getApiKey() {
		return this.apiKey;
	}
	
	/**
	 * Set API key to use for client authentication by Untappd.
	 * 
	 * @param value Value.
	 */
	public void setApiKey(String value) {
		this.apiKey = value;
	}

	/**
	 * Use GSON to deserialize a JSON object to a native class representation.
	 * 
	 * @param <T> Native class type.
	 * @param typeToken Native class type wrapper.
	 * @param response Serialized JSON object.
	 * @return Deserialized native instance.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T unmarshall(TypeToken<T> typeToken, JsonElement response) {
		return (T)UntappdApiService.getGsonBuilder().create().fromJson(response, typeToken.getType());
	}
	
	/**
	 * Use GSON to deserialize a JSON string to a native class representation.
	 * 
	 * @param <T> Native class type.
	 * @param typeToken Native class type wrapper.
	 * @param reponse Serialized JSON string.
	 * @return Deserialized native instance.
	 */
	@SuppressWarnings("unchecked")
    protected <T> T unmarshall(TypeToken<T> typeToken, String reponse) {
	    return (T)UntappdApiService.getGsonBuilder().create().fromJson(reponse, typeToken.getType());
	}
	
	/**
	 * Read the entirety of an input stream and parse to a JSON object.
	 * 
	 * @param jsonContent JSON content input stream.
	 * @return Parsed JSON object.
	 */
	protected JsonElement unmarshall(InputStream jsonContent) {
        try {
        	JsonElement element = this.parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
        	if (element.isJsonObject()) {
        		return element.getAsJsonObject();
        	} else if (element.isJsonArray()) {
        		return element.getAsJsonArray();
        	} else {
        		throw new ApiException("Unknown content found in response." + element);
        	}
        } catch (Exception e) {
            throw new ApiException(e);
        } finally {
	        ApiService.closeStream(jsonContent);
	    }
	}

	/**
	 * Create a {@link GsonBuilder} and register all of the custom types needed
	 * in order to properly deserialize complex Trakt-specific type.
	 * 
	 * @return Assembled GSON builder instance.
	 */
	static GsonBuilder getGsonBuilder() {
		GsonBuilder builder = new GsonBuilder();
		
		//class types
		//TODO
		
		//enum types
		//TODO
		
		return builder;
	}
}
