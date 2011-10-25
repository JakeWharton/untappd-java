package com.jakewharton.untappd;

import com.google.gson.JsonObject;
import com.jakewharton.apibuilder.ApiException;
import com.jakewharton.untappd.entities.Response;

public final class UntappdException extends RuntimeException {
    private static final long serialVersionUID = 6158978902757706299L;

    private final String url;
    private final JsonObject postBody;
    private final Response response;

    public UntappdException(String url, ApiException cause) {
        this(url, null, cause);
    }
    public UntappdException(String url, JsonObject postBody, ApiException cause) {
        super(cause);
        this.url = url;
        this.postBody = postBody;
        this.response = null;
    }
    public UntappdException(String url, JsonObject postBody, ApiException cause, Response response) {
        super(response.getError(), cause);
        this.url = url;
        this.postBody = postBody;
        this.response = response;
    }

    public String getUrl() {
        return this.url;
    }
    public JsonObject getPostBody() {
        return this.postBody;
    }
    public Response getResponse() {
        return this.response;
    }
}
