/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.yamahamusiccast.internal.api;

import java.net.ConnectException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.MimeTypes;
import org.openhab.binding.yamahamusiccast.internal.api.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

/**
 * The {@link MusicCastRequest} encapsulates a request sent by the {@link YamahaMusicCast}.
 *
 * @author Matthew Bowman - Initial contribution
 *
 * @param <T> The response type expected as a result of the request's execution
 */
@NonNullByDefault
public class MusicCastRequest<T> {

    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final String CONTENT_TYPE_APPLICATION_JSON = MimeTypes.Type.APPLICATION_JSON.asString();

    private static final long TIMEOUT_SECONDS = 5;

    private static final String PROPERTY_DATA = "data";

    private final Logger logger = LoggerFactory.getLogger(MusicCastRequest.class);

    private Gson gson;

    private HttpClient httpClient;

    private String host = "yamaha";

    private int port = 80;

    private String path = "/";

    private Map<String, String> queryParameters = new HashMap<>();

    private Map<String, String> bodyParameters = new HashMap<>();

    private Map<String, String> requestHeaders = new HashMap<>();

    private Class<T> resultType;

    // Public API

    public MusicCastRequest(Class<T> resultType, Gson gson, HttpClient httpClient, String host, int port) {
        this.resultType = resultType;
        this.gson = gson;
        this.httpClient = httpClient;
        this.host = host;
        this.port = port;
    }

    public MusicCastRequest(Gson gson, HttpClient httpClient, String host, int port) {
        this.gson = gson;
        this.httpClient = httpClient;
        this.host = host;
        this.port = port;
        this.resultType = (Class<T>) Object.class;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setResultType(Class<T> resultType) {
        this.resultType = resultType;
    }

    public void setBodyParameter(String key, Object value) {
        this.bodyParameters.put(key, String.valueOf(value));
    }

    public void setQueryParameter(String key, Object value) {
        this.queryParameters.put(key, String.valueOf(value));
    }

    public void setHeaders(String key, Object value) {
        this.requestHeaders.put(key, String.valueOf(value));
    }

    public void clearBodyParameter() {
        this.bodyParameters.clear();
    }

    public void clearQueryParameter() {
        this.queryParameters.clear();
    }

    public void clearHeaders() {
        this.requestHeaders.clear();
    }

    public <@Nullable T extends @Nullable Response> @Nullable T execute() throws MusicCastException {
        if (resultType == null) {
            return null;
        }

        T result = null;
        String json = getContent();
        logger.debug("JSON is " + json);
        // mgb: only try and unmarshall non-void result types
        if (!Void.class.equals(resultType)) {
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            logger.debug("JsonObject: " + jsonObject.toString());
            if (jsonObject.has(PROPERTY_DATA)) {
                logger.debug("JsonObject has Property data");
                if (jsonObject.get(PROPERTY_DATA).isJsonArray()) {
                    logger.debug("Object is array.");
                    result = (T) gson.fromJson(jsonObject.getAsJsonArray(PROPERTY_DATA), resultType);
                } else {
                    result = (T) gson.fromJson(jsonObject.getAsJsonObject(PROPERTY_DATA), resultType);
                }
            } else {
                result = (T) gson.fromJson(json, resultType);
                // Check the result type and throw an exception if not succesful
                /**
                 * code description
                 * 0 Successful request
                 * 1 Initializing
                 * 2 Internal Error
                 * 3 Invalid Request (A method did not exist, a method wasn’t appropriate etc.)
                 * 4 Invalid Parameter (Out of range, invalid characters etc.)
                 * 5 Guarded (Unable to setup in current status etc.)
                 * 6 Time Out
                 * 99 Firmware Updating
                 * (100s are Streaming Service related errors)
                 * 100 Access Error
                 */
                switch (result.getResponseCode()) {
                    case 0: {
                        // Great
                        break;
                    }
                    case 1: {
                        throw new MusicCastException("Initializing");
                    }
                    case 2: {
                        throw new MusicCastException("Internal Error");
                    }
                    case 3: {
                        throw new MusicCastException(
                                "Invalid Request (A method did not exist, a method wasn’t appropriate etc.)");
                    }
                    case 4: {
                        throw new MusicCastException("Invalid Parameter (Out of range, invalid characters etc.)");
                    }
                    case 5: {
                        throw new MusicCastException("Guarded (Unable to setup in current status etc.)");
                    }
                    case 6: {
                        throw new MusicCastException("Timeout");
                    }
                    case 99: {
                        throw new MusicCastException("Firmware Updating");
                    }
                    case 100: {
                        throw new MusicCastException("Access Error");
                    }
                    default: {
                        throw new MusicCastException("Unknown MusicCast id : " + result.getResponseCode());
                    }
                }
            }
        } else {
            logger.error("ResultType is void!");
        }
        return result;
    }

    // Private API

    private String getContent() throws MusicCastException {
        String content;
        ContentResponse response = getContentResponse();
        int status = response.getStatus();
        switch (status) {
            case HttpStatus.OK_200:
                content = response.getContentAsString();
                if (logger.isTraceEnabled()) {
                    logger.trace("<< {} {} \n{}", status, HttpStatus.getMessage(status), prettyPrintJson(content));
                }
                break;
            case HttpStatus.BAD_REQUEST_400:
                throw new MusicCastException("Invalid Credentials");
            case HttpStatus.UNAUTHORIZED_401:
                throw new MusicCastException("Expired Credentials");
            default:
                throw new MusicCastException("Unknown HTTP status code " + status + " returned by the controller");
        }
        return content;
    }

    private ContentResponse getContentResponse() throws MusicCastException {
        Request request = newRequest();
        logger.debug(">> {} {}", request.getMethod(), request.getURI());
        ContentResponse response;
        try {
            response = request.send();
        } catch (TimeoutException | InterruptedException e) {
            throw new MusicCastException(e);
        } catch (ExecutionException e) {
            // mgb: unwrap the cause and try to cleanly handle it
            Throwable cause = e.getCause();
            if (cause instanceof ConnectException) {
                throw new MusicCastException(cause);
            } else {
                throw new MusicCastException(cause);
            }
        }
        return response;
    }

    private Request newRequest() {
        HttpMethod method = bodyParameters.isEmpty() ? HttpMethod.GET : HttpMethod.POST;
        HttpURI uri = new HttpURI(HttpScheme.HTTP.asString(), host, port, path);
        Request request = httpClient.newRequest(uri.toString()).timeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .method(method);
        for (Entry<String, String> entry : queryParameters.entrySet()) {
            request.param(entry.getKey(), entry.getValue());
        }
        if (!requestHeaders.isEmpty()) {
            for (Entry<String, String> entry : requestHeaders.entrySet()) {
                request.header(entry.getKey(), entry.getValue());
            }
        }
        if (!bodyParameters.isEmpty()) {
            String jsonBody = getRequestBodyAsJson();
            ContentProvider content = new StringContentProvider(CONTENT_TYPE_APPLICATION_JSON, jsonBody, CHARSET_UTF8);
            request = request.content(content);
        }
        return request;
    }

    private String getRequestBodyAsJson() {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonElement = null;
        for (Entry<String, String> entry : bodyParameters.entrySet()) {
            try {
                jsonElement = jsonParser.parse(entry.getValue());
            } catch (JsonSyntaxException e) {
                jsonElement = new JsonPrimitive(entry.getValue());
            }
            jsonObject.add(entry.getKey(), jsonElement);
        }
        return jsonObject.toString();
    }

    private static String prettyPrintJson(String content) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(content).getAsJsonObject();
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        return prettyGson.toJson(json);
    }
}
